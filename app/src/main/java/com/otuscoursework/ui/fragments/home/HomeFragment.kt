package com.otuscoursework.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.otuscoursework.R
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState
import com.otuscoursework.arch.recycler.BaseDelegationAdapter
import com.otuscoursework.arch.recycler.RecyclerViewItem
import com.otuscoursework.databinding.FragmentHomeBinding
import com.otuscoursework.databinding.ViewPopupShowFavouriteBinding
import com.otuscoursework.di.components.DaggerFragmentHomeComponent
import com.otuscoursework.di.components.FragmentHomeComponent
import com.otuscoursework.ui.CartKeeper
import com.otuscoursework.ui.fragments.dialog.OtusDialogFragment
import com.otuscoursework.ui.fragments.menuItemDetail.MenuItemDetailFragment
import com.otuscoursework.ui.main.MainActivity
import com.otuscoursework.ui.models.ChipItemUiModel
import com.otuscoursework.ui.models.MenuItemModel
import com.otuscoursework.ui.models.MenuItemUiModel
import com.otuscoursework.utils_and_ext.OtusLogger
import com.otuscoursework.utils_and_ext.setSafeOnClickListener
import com.otuscoursework.view.badge_button.ButtonType
import com.otuscoursework.view.size_changer.SizeChanger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentViewModel>() {

    private lateinit var fragmentBinding: FragmentHomeBinding
    override val viewModel: HomeFragmentViewModel by viewModels()

    private val menuItemModelList: MutableList<MenuItemModel> = mutableListOf()
    private val chipsItemUiModelList: MutableList<ChipItemUiModel> = mutableListOf()
    private var selectedSizeIndex = SizeChanger.INDEX_CENTER

    private val categoryIdChipList: MutableList<Int> = mutableListOf()
    private val chipsAdapter = createChipsAdapter()
    private val menuAdapter = createMenuAdapter()

    private var isFavouriteModeActive = MutableStateFlow(false)

    @Inject
    lateinit var cartKeeper: CartKeeper

    lateinit var fragmentComponent: FragmentHomeComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentComponent = DaggerFragmentHomeComponent
            .factory()
            .create(MainActivity.INSTANCE.activityComponent)
    }

    override fun initViews() {
        initButtons()
        initRecyclerViews()
        initPopupMenu()
        initSizeChanger()
    }

    override fun initObservers() {
        super.initObservers()
        lifecycleScope.launch {
            cartKeeper.getFlow().collect {
                fragmentBinding.toCartButton.updateBadge(it.size)
            }
        }

        lifecycleScope.launch {
            isFavouriteModeActive.collect {
                onModeChanged()
            }
        }
    }

    private fun initButtons() {
        fragmentBinding.apply {
            toCartButton.apply {
                setButtonType(ButtonType.CART)
                setSafeOnClickListener { ciceroneAppNavigator.toCartScreen() }
            }

            toOrdersButton.apply {
                setButtonType(ButtonType.LIST)
                setSafeOnClickListener { ciceroneAppNavigator.toOrdersScreen() }
            }

            toNotificationButton.apply {
                setButtonType(ButtonType.NOTIFICATION)
                setSafeOnClickListener { ciceroneAppNavigator.toNotificationScreen() }
            }

            toSaleButton.apply {
                setButtonType(ButtonType.SALE)
                setSafeOnClickListener { ciceroneAppNavigator.toSaleScreen() }
            }

            toAddTokensButton.apply {
                setButtonType(ButtonType.ADD_TOKENS)
                setSafeOnClickListener { showAddTokensDialog() }
            }
        }
    }

    private fun initRecyclerViews() {
        fragmentBinding.apply {
            chipsRecyclerView.apply {
                adapter = chipsAdapter
                val linearLayoutManager = LinearLayoutManager(requireContext())
                linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
                layoutManager = linearLayoutManager
            }
            menuRecyclerView.apply {
                adapter = menuAdapter
                layoutManager = GridLayoutManager(requireContext(), 2)
            }
        }
    }

    private fun initPopupMenu() {
        val popupBinding = ViewPopupShowFavouriteBinding.inflate(
            LayoutInflater.from(context), null, false
        )
        val bodyWindow: PopupWindow = PopupWindow(
            popupBinding.root,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            isFocusable = true
        }

        fragmentBinding.showOptionsButton.setSafeOnClickListener {
            bodyWindow.showAsDropDown(fragmentBinding.showOptionsButton, 0, 0);
        }

        popupBinding.apply {
            showOptionsShowAllOption.setSafeOnClickListener {
                isFavouriteModeActive.value = false
                bodyWindow.dismiss()
            }
            showOptionsShowFavouriteOption.setSafeOnClickListener {
                isFavouriteModeActive.value = true
                bodyWindow.dismiss()
            }
        }
    }

    private fun initSizeChanger() {
        fragmentBinding.sizeChanger.apply {
            setOnCheckedChangeListener {
                changeSelectedSize(it)
            }
            setItemNames(
                listOf(
                    getString(R.string.sizeLeft),
                    getString(R.string.sizeCenter),
                    getString(R.string.sizeRight),
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        HomeFragmentState(isLoading = false)
        fragmentComponent.inject(this)
        fragmentBinding = FragmentHomeBinding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.onOpen()
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

        (state as HomeFragmentState).apply {

            fragmentBinding.balancePanelBalance.text = tokenAmount.toString()

            chipsItemList.forEach {
                it.isSelected = it.id in categoryIdChipList
            }

            chipsItemUiModelList.clear()
            chipsItemUiModelList.addAll(chipsItemList)
            populateChipsMenu()

            menuItemModelList.clear()
            menuItemModelList.addAll(menuItemList)
            populateMenuRecyclerView()
        }
    }

    private fun showAddTokensDialog() {
        val dialog = OtusDialogFragment.newInstance(OtusDialogFragment.DialogType.ADD_PROMO)
        dialog.show(activity?.supportFragmentManager!!, OtusDialogFragment.DIALOG_TAG)
    }

    private fun showBottomSheetMenuItemDetail(item: MenuItemUiModel) {
        val dialog = MenuItemDetailFragment.newInstance(
            item = menuItemModelList.first { it.id == item.id },
            index = selectedSizeIndex,
            changeFavouriteStatus = { changeFavouriteStatus(item) },
            addItemToCart = {
                addItemToCart(item, it)
            },
            removeItemFromCart = {
                removeItemFromCart(item, it)
            })
        dialog.show(activity?.supportFragmentManager!!, MenuItemDetailFragment.DIALOG_TAG)
    }

    private fun changeSelectedSize(index: Int) {
        selectedSizeIndex = index
        populateMenuRecyclerView()
    }

    private fun createChipsAdapter(): BaseDelegationAdapter {
        return BaseDelegationAdapter(
            ChipAdapterDelegate.chipDelegate(
                filterItems = ::filterMenuItem
            )
        )
    }

    private fun createMenuAdapter(): BaseDelegationAdapter {
        return BaseDelegationAdapter(
            MenuAdapterDelegate.menuDelegate(
                showDetails = ::showBottomSheetMenuItemDetail,
                addToCart = ::addItemToCart,
                removeFromCart = ::removeItemFromCart,
                changeFavouriteStatus = ::changeFavouriteStatus
            )
        )
    }

    private fun updateMenuItem(item: MenuItemUiModel) {
        val index = menuAdapter.items.indexOf(item)
        menuAdapter.notifyItemChanged(index)
    }

    private fun addItemToCart(item: MenuItemUiModel, selectedSize: Int? = null) {
        lifecycleScope.launch {
            val index = menuItemModelList.indexOfFirst { it.id == item.id }
            menuItemModelList[index].amountInCart[selectedSize ?: selectedSizeIndex] =
                menuItemModelList[index].amountInCart[selectedSize ?: selectedSizeIndex] + 1

            if (menuItemModelList[index].sizes[selectedSize ?: selectedSizeIndex] == item.size) {
                item.amountInCart = item.amountInCart + 1
                updateMenuItem(item)
            }

            menuItemModelList[index].let {
                cartKeeper.addItemToCart(
                    item = MenuItemUiModel(
                        id = it.id,
                        name = it.name,
                        categoryId = it.categoryId,
                        picture = it.picture,
                        description = it.description,
                        isInFavourite = it.isInFavourite,
                        size = it.sizes[selectedSize ?: selectedSizeIndex],
                        amountInCart = it.amountInCart[selectedSize ?: selectedSizeIndex]
                    ).toCartItem()
                )
            }
        }
    }

    private fun removeItemFromCart(item: MenuItemUiModel, selectedSize: Int? = null) {
        lifecycleScope.launch {
            val index = menuItemModelList.indexOfFirst { it.id == item.id }
            menuItemModelList[index].amountInCart[selectedSize ?: selectedSizeIndex] =
                menuItemModelList[index].amountInCart[selectedSize ?: selectedSizeIndex] - 1

            if (menuItemModelList[index].sizes[selectedSize ?: selectedSizeIndex] == item.size) {
                item.amountInCart = item.amountInCart - 1
                updateMenuItem(item)
            }

            menuItemModelList[index].let {
                cartKeeper.removeItemFromCart(
                    item = MenuItemUiModel(
                        id = it.id,
                        name = it.name,
                        categoryId = it.categoryId,
                        picture = it.picture,
                        description = it.description,
                        isInFavourite = it.isInFavourite,
                        size = it.sizes[selectedSize ?: selectedSizeIndex],
                        amountInCart = it.amountInCart[selectedSize ?: selectedSizeIndex]
                    ).toCartItem()
                )
            }
        }
    }

    private fun changeFavouriteStatus(item: MenuItemUiModel) {
        val index = menuItemModelList.indexOfFirst { it.id == item.id }
        item.isInFavourite = menuItemModelList[index].isInFavourite
        if (isFavouriteModeActive.value) populateMenuRecyclerView() else updateMenuItem(item)
    }

    private fun onModeChanged() {
        fragmentBinding.showOptionsButton.text = if (isFavouriteModeActive.value) {
            getString(R.string.showOptionsShowFavourite)
        } else {
            getString(R.string.showOptionsShowAll)
        }

        populateMenuRecyclerView()
    }

    private fun filterMenuItem(chipItemUiModel: ChipItemUiModel) {
        chipsAdapter.notifyItemChanged(chipItemUiModel.id)
        OtusLogger.log("$chipItemUiModel was pressed")

        if (chipItemUiModel.isSelected) {
            categoryIdChipList.add(chipItemUiModel.id)
        } else {
            categoryIdChipList.remove(chipItemUiModel.id)
        }

        OtusLogger.log("categoryIdChipList is $categoryIdChipList")
        populateMenuRecyclerView()
    }

    private fun populateChipsMenu() {
        chipsAdapter.apply {
            items = chipsItemUiModelList as List<RecyclerViewItem>?
            notifyDataSetChanged()
        }
    }

    private fun populateMenuRecyclerView() {
        menuAdapter.apply {
            items = (if (isFavouriteModeActive.value) {
                if (categoryIdChipList.isNotEmpty()) {
                    menuItemModelList.filter { it.categoryId in categoryIdChipList && it.isInFavourite }
                } else {
                    menuItemModelList.filter { it.isInFavourite }
                }
            } else {
                if (categoryIdChipList.isNotEmpty()) {
                    menuItemModelList.filter { it.categoryId in categoryIdChipList }
                } else {
                    menuItemModelList
                }
            }).toUi()
            notifyDataSetChanged()
        }
    }

    private fun List<MenuItemModel>.toUi(): List<MenuItemUiModel> {
        return this.map {
            MenuItemUiModel(
                id = it.id,
                name = it.name,
                categoryId = it.categoryId,
                picture = it.picture,
                description = it.description,
                isInFavourite = it.isInFavourite,
                size = it.sizes[selectedSizeIndex],
                amountInCart = it.amountInCart[selectedSizeIndex]
            )
        }
    }
}