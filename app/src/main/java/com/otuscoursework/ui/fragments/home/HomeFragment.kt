package com.otuscoursework.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.otuscoursework.R
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState
import com.otuscoursework.arch.recycler.BaseDelegationAdapter
import com.otuscoursework.databinding.FragmentHomeBinding
import com.otuscoursework.databinding.PopupShowFavouriteBinding
import com.otuscoursework.ui.CartKeeper
import com.otuscoursework.ui.models.CartCheckItemUiModel
import com.otuscoursework.ui.models.ChipItemUiModel
import com.otuscoursework.ui.models.MenuItemUiModel
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
                onModeChanged(it)
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
        fragmentBinding.chipsRecyclerView.adapter = chipsAdapter
        fragmentBinding.menuRecyclerView.adapter = menuAdapter
    }

    private fun initPopupMenu() {
        val binding: PopupShowFavouriteBinding = PopupShowFavouriteBinding.inflate(
            LayoutInflater.from(context), null, false
        )
        val bodyWindow: PopupWindow = PopupWindow(
            binding.root,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            isFocusable = true
        }

        fragmentBinding.showOptionsButton.setSafeOnClickListener {
            bodyWindow.showAsDropDown(fragmentBinding.showOptionsButton, 0, -50);
        }

        binding.showOptionsShowAllOption.setSafeOnClickListener {
            isFavouriteModeActive.value = false
            bodyWindow.dismiss()
        }
        binding.showOptionsShowFavouriteOption.setSafeOnClickListener {
            isFavouriteModeActive.value = true
            bodyWindow.dismiss()
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

            chipsAdapter.apply {
                items = chipsItemList
                notifyDataSetChanged()
            }

            menuAdapter.apply {
                items = menuItemList
                onModeChanged(isFavouriteModeActive.value)
            }
        }
    }


    private fun showAddTokensDialog() {

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
                ::removeItemFromCart
            )
        )
    }

    private fun filterMenuItem(chipItemUiModel: ChipItemUiModel) {
        chipsAdapter.notifyItemChanged(chipItemUiModel.id)
        onModeChanged(isFavouriteModeActive.value)
    }

    private fun addItemToCart(newItem: CartCheckItemUiModel) {
        lifecycleScope.launch {
            cartKeeper.addItemToCart(newItem = newItem)
        }
    }

    private fun removeItemFromCart(item: CartCheckItemUiModel) {
        lifecycleScope.launch {
            cartKeeper.removeItemFromCart(item = item)
        }
    }

    private fun onModeChanged(isFavouriteMode: Boolean) {
        fragmentBinding.showOptionsButton.text = if (isFavouriteMode) {
            getString(R.string.showOptionsShowFavourite)
        } else {
            getString(R.string.showOptionsShowAll)
        }

        menuAdapter.apply {
            val oldItems = items as List<MenuItemUiModel>

            items = if (isFavouriteMode) {
                if (categoryIdChipList.isNotEmpty()) {
                    oldItems.filter { it.categoryId in categoryIdChipList && it.isInFavourite }
                } else {
                    oldItems.filter { it.isInFavourite }
                }
            } else {
                if (categoryIdChipList.isNotEmpty()) {
                    oldItems.filter { it.categoryId in categoryIdChipList }
                } else {
                    oldItems
                }
            }
            notifyDataSetChanged()
        }
    }
}