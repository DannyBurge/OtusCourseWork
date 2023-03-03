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
import com.otuscoursework.ui.CartKeeper
import com.otuscoursework.ui.fragments.dialog.OtusDialogFragment
import com.otuscoursework.ui.models.ChipItemUiModel
import com.otuscoursework.ui.models.MenuItemUiModel
import com.otuscoursework.utils_and_ext.OtusLogger
import com.otuscoursework.utils_and_ext.setSafeOnClickListener
import com.otuscoursework.view.badge_button.ButtonType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentViewModel>() {

    private lateinit var fragmentBinding: FragmentHomeBinding
    override val viewModel: HomeFragmentViewModel by viewModels()

    private val categoryIdChipList: MutableList<Int> = mutableListOf()
    private val chipsAdapter = createChipsAdapter()
    private val menuAdapter = createMenuAdapter()

    private val menuItemUiModelList: MutableList<MenuItemUiModel> = mutableListOf()
    private val chipsItemUiModelList: MutableList<ChipItemUiModel> = mutableListOf()

    private var isFavouriteModeActive = MutableStateFlow(false)

    @Inject
    lateinit var cartKeeper: CartKeeper

    override fun initViews() {
        initButtons()
        initRecyclerViews()
        initPopupMenu()
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

            menuItemUiModelList.clear()
            menuItemUiModelList.addAll(menuItemList)
            populateMenuRecyclerView()
        }
    }

    private fun showAddTokensDialog() {
        val dialog = OtusDialogFragment.newInstance(OtusDialogFragment.DialogType.ADD_PROMO)
        dialog.show(activity?.supportFragmentManager!!, "")
    }

    private fun createChipsAdapter(): BaseDelegationAdapter {
        return BaseDelegationAdapter(
            ChipAdapterDelegate.chipDelegate(
                ::filterMenuItem
            )
        )
    }

    private fun createMenuAdapter(): BaseDelegationAdapter {
        return BaseDelegationAdapter(
            MenuAdapterDelegate.menuDelegate(
                ::addItemToCart,
                ::removeItemFromCart,
                ::changeFavouriteStatus
            )
        )
    }

    private fun addItemToCart(item: MenuItemUiModel) {
        lifecycleScope.launch {
            item.amountInCart = item.amountInCart + 1
//TODO item.id при выбранном режиме Избранное не совпадает с позицией
            menuAdapter.notifyItemChanged(item.id)
            cartKeeper.addItemToCart(newItem = item.toCartItem())
        }
    }

    private fun removeItemFromCart(item: MenuItemUiModel) {
        lifecycleScope.launch {
            item.amountInCart = item.amountInCart - 1
//TODO item.id при выбранном режиме Избранное не совпадает с позицией
            menuAdapter.notifyItemChanged(item.id)
            cartKeeper.removeItemFromCart(item = item.toCartItem())
        }
    }

    private fun changeFavouriteStatus(item: MenuItemUiModel) {
        menuAdapter.notifyItemChanged(item.id)
        populateMenuRecyclerView()
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
            items = if (isFavouriteModeActive.value) {
                if (categoryIdChipList.isNotEmpty()) {
                    menuItemUiModelList.filter { it.categoryId in categoryIdChipList && it.isInFavourite }
                } else {
                    menuItemUiModelList.filter { it.isInFavourite }
                }
            } else {
                if (categoryIdChipList.isNotEmpty()) {
                    menuItemUiModelList.filter { it.categoryId in categoryIdChipList }
                } else {
                    menuItemUiModelList
                }
            }
            notifyDataSetChanged()
        }
    }
}