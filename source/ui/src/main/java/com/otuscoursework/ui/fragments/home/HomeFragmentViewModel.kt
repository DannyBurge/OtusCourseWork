package com.otuscoursework.ui.fragments.home

import androidx.lifecycle.viewModelScope
import com.otuscourcework.cart_keeper.CartKeeper
import com.otuscourcework.network.NetworkRepository
import com.otuscourcework.network.models.BalanceHistoryItem
import com.otuscourcework.network.models.CategoryItem
import com.otuscourcework.network.models.MenuItem
import com.otuscourcework.user_data_keeper.UserDataKeeper
import com.otuscourcework.utils.toFullIsoDate
import com.otuscoursework.resource.R
import com.otuscoursework.ui.arch.BaseViewModel
import com.otuscoursework.ui.fragments.home.ui_model.ChipItemUiModel
import com.otuscoursework.ui.fragments.home.ui_model.MenuItemUiModel
import com.otuscoursework.ui.models.MenuItemDetailModel
import com.otuscoursework.ui.models.MenuItemFoodValueModel
import com.otuscoursework.ui.models.MenuSubItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    val networkRepository: NetworkRepository
) : BaseViewModel<HomeFragmentState>() {
    @Inject
    lateinit var cartKeeper: CartKeeper

    @Inject
    lateinit var userDataKeeper: UserDataKeeper

    val selectedCategories: MutableList<Int> = mutableListOf()

    private val _cartSize = MutableStateFlow(0)
    val cartSize: StateFlow<Int> = _cartSize

    private val _isFavouriteModeActive = MutableSharedFlow<Boolean>()
    val isFavouriteModeActive: SharedFlow<Boolean> = _isFavouriteModeActive

    override var viewModelState = HomeFragmentState()

    private var balanceRaw: List<BalanceHistoryItem>? = null
    private var chipsRaw: List<CategoryItem>? = null
    private var menuRaw: List<MenuItem>? = null

    fun onOpen() {
        viewModelScope.launch(Dispatchers.IO) {
            _isFavouriteModeActive.emit(getFavouriteModeStatus())
        }

        viewModelScope.launch(Dispatchers.IO) {
            cartKeeper.cartSize.collect {
                _cartSize.emit(it)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {

            viewModelState = viewModelState.copy(isLoading = true).apply { render() }

            val validKey = networkRepository.getApiKey()

            if (validKey == null) {
                viewModelState = viewModelState.copy(
                    isLoading = false,
                    errorMessage = getStringById(R.string.server_error)
                ).apply { render() }
                return@launch
            }

            userDataKeeper.apiToken = validKey.key


            val balanceCall = async { networkRepository.getUserBalanceHistory(true) }
            val chipsCall = async { networkRepository.getCategories() }
            val menuCall = async { networkRepository.getMenu() }

            balanceRaw = balanceCall.await()
            chipsRaw = chipsCall.await()
            menuRaw = menuCall.await()

            if (balanceRaw == null || chipsRaw == null || menuRaw == null) {
                viewModelState = viewModelState.copy(
                    isLoading = false,
                    errorMessage = getStringById(R.string.server_error)
                ).apply { render() }
                return@launch
            }
            viewModelState = viewModelState.copy(
                isLoading = false,
                tokenAmount = balanceRaw!!.last().amount!!,
                chipsItemList = chipsRaw!!.map { it.toUi() },
                menuItemList = menuRaw!!.map { it.toUi() },
            ).apply { render() }
        }
    }

    fun saveFavouriteModeStatus(isActive: Boolean) {
        userDataKeeper.isFavouriteModeActive = isActive
        viewModelScope.launch(Dispatchers.IO) {
            _isFavouriteModeActive.emit(isActive)
        }
    }

    fun getFavouriteModeStatus(): Boolean {
        return userDataKeeper.isFavouriteModeActive
    }

    fun saveTokensAmount(amount: Int) {
        userDataKeeper.tokensAmount = amount
    }

    fun getMenuItemDetailModel(id: Int): MenuItemDetailModel? {
        val rawItem = menuRaw?.firstOrNull { it.groupId == id } ?: return null

        val subItems = rawItem.subItems.map {
            MenuSubItemModel(
                id = it.id,
                price = it.price,
                displayName = it.displayName,
                weight = it.weight,
            )
        }
        val amountInCart = rawItem.subItems.map {
            cartKeeper.getCartContent()[it.id]?.amount ?: 0
        }

        return MenuItemDetailModel(
            groupId = rawItem.groupId,
            name = rawItem.name,
            categoryId = rawItem.categoryId,
            picture = rawItem.picture,
            description = rawItem.description,
            isInFavourite = userDataKeeper.userFavouriteItemIds?.contains(rawItem.groupId) == true,
            subItems = subItems,
            amountInCart = amountInCart.toMutableList(),
            foodValue = MenuItemFoodValueModel(
                ccal = rawItem.foodValue.ccal,
                proteins = rawItem.foodValue.proteins,
                fats = rawItem.foodValue.fats,
                carbohydrates = rawItem.foodValue.carbohydrates
            )
        )
    }

    fun changeItemFavouriteStatus(item: MenuItemUiModel) {
        val favList = (userDataKeeper.userFavouriteItemIds)?.toMutableList() ?: mutableListOf()
        if (item.isInFavourite) {
            favList.add(item.id)
        } else {
            favList.remove(item.id)
        }
        userDataKeeper.userFavouriteItemIds = favList
        if (favouriteListIsEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                _isFavouriteModeActive.emit(false)
            }
        }
    }

    fun setPromoCode(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            viewModelState = viewModelState.copy(
                isLoading = true,
            ).apply { render() }

            val validateCode = networkRepository.validateCode(code = code)

            if (validateCode == null) {
                viewModelState = viewModelState.copy(
                    isLoading = false,
                    errorMessage = getStringById(R.string.server_error)
                ).apply { render() }
                return@launch
            }

            if (validateCode.validated) {
                val balance = networkRepository.addBalance(
                    BalanceHistoryItem(
                        date = Date().toFullIsoDate(),
                        amountAdded = validateCode.amountAdded,
                    )
                )
                if (balance == null) {
                    viewModelState = viewModelState.copy(
                        isLoading = false,
                        errorMessage = getStringById(R.string.server_error)
                    ).apply { render() }
                    return@launch
                }

                viewModelState = viewModelState.copy(
                    isLoading = false,
                    tokenAmount = balance.amount!!
                ).apply { render() }
            } else {
                viewModelState = viewModelState.copy(
                    isLoading = false,
                    errorMessage = getStringById(R.string.promo_code_not_valid)
                ).apply { render() }
                return@launch
            }
        }
    }

    private fun favouriteListIsEmpty(): Boolean {
        val isEmpty = userDataKeeper.userFavouriteItemIds.isNullOrEmpty()
        if (isEmpty) saveFavouriteModeStatus(isActive = false)
        return isEmpty
    }

    private fun CategoryItem.toUi(): ChipItemUiModel {
        return ChipItemUiModel(
            id = this.id,
            name = this.name,
            isSelected = false
        )
    }

    private fun MenuItem.toUi(): MenuItemUiModel {
        return MenuItemUiModel(
            id = this.groupId,
            subItemIds = this.subItems.map { it.id },
            name = this.name,
            categoryId = this.categoryId,
            picture = this.picture,
            description = this.description,
            isInFavourite = userDataKeeper.userFavouriteItemIds?.contains(this.groupId) == true,
            price = this.subItems.first().price
        )
    }
}