package com.otuscoursework.ui.fragments.home

import android.annotation.SuppressLint
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otuscoursework.R
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState
import com.otuscoursework.arch.recycler.BaseDelegationAdapter
import com.otuscoursework.arch.recycler.RecyclerViewItem
import com.otuscoursework.databinding.FragmentHomeBinding
import com.otuscoursework.di.components.DaggerFragmentHomeComponent
import com.otuscoursework.di.components.FragmentHomeComponent
import com.otuscoursework.ui.fragments.dialog.OtusDialogFragment
import com.otuscoursework.ui.fragments.menuItemDetail.MenuItemDetailFragment
import com.otuscoursework.ui.main.MainActivity
import com.otuscoursework.ui.models.*
import com.otuscoursework.utils_and_ext.OtusLogger
import com.otuscoursework.utils_and_ext.setSafeOnClickListener
import com.otuscoursework.view.badge_button.ButtonType
import com.otuscoursework.view.popup.PopupMenuWithList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<HomeFragmentViewModel>(),
    PopupMenuWithList.OnPopupMenuItemClickListener {

    override lateinit var fragmentBinding: FragmentHomeBinding

    override val viewModel: HomeFragmentViewModel by viewModels()

    private val menuItemUiModelList: MutableList<MenuItemUiModel> = mutableListOf()
    private val chipsItemUiModelList: MutableList<ChipItemUiModel> = mutableListOf()

    private val chipsAdapter = createChipsAdapter()
    private val menuAdapter = createMenuAdapter()

    private lateinit var popupMenu: PopupMenuWithList

    lateinit var fragmentComponent: FragmentHomeComponent

    override fun initViews() {
        initButtons()
        initRecyclerViews()
        initPopupMenu()
    }

    override fun initFragmentComponent() {
        fragmentComponent = DaggerFragmentHomeComponent
            .factory()
            .create(MainActivity.INSTANCE.activityComponent)
        fragmentComponent.inject(this)
    }

    override fun initObservers() {
        super.initObservers()
        lifecycleScope.launch {
            viewModel.cartSize.collect {
                fragmentBinding.toCartButton.updateBadge(it)
            }
        }

        lifecycleScope.launch {
            viewModel.isFavouriteModeActive.collect {
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
                val screenWidth = resources.displayMetrics.widthPixels
                val itemMinWidth = resources.getDimensionPixelSize(R.dimen.main_size_150)
                val sizeMargin = resources.getDimensionPixelSize(R.dimen.margin_big)
                val sizeMarginMedium = resources.getDimensionPixelSize(R.dimen.margin_medium)
                val recyclerViewWidth = screenWidth - sizeMargin * 2
                val columnNumber = recyclerViewWidth / itemMinWidth

                adapter = menuAdapter
                layoutManager = GridLayoutManager(requireContext(), columnNumber)
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    val lastRowItemNumber = if (menuAdapter.items.size % columnNumber == 0) {
                        columnNumber
                    } else {
                        menuAdapter.items.size % columnNumber
                    }

                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        val position = parent.getChildAdapterPosition(view) // item position
                        val column = position % columnNumber // item column

                        outRect.left = sizeMargin - column * sizeMargin / columnNumber
                        outRect.right = (column + 1) * sizeMargin / columnNumber

                        if (position < columnNumber) {
                            outRect.top = sizeMarginMedium
                        } else {
                            outRect.top = sizeMargin
                        }

                        if (position > parent.adapter!!.itemCount - lastRowItemNumber - 1) {
                            outRect.bottom = sizeMargin
                        }
                    }
                })
            }
        }
    }

    private fun initPopupMenu() {
        setCorrectModeName()

        val menuHeader = getString(R.string.popupMenuTitle)
        val menuItemList = listOf(
            PopupUiItem(
                id = SHOW_FAVOURITE_ID,
                name = getString(R.string.showOptionsShowFavourite)
            ),
            PopupUiItem(
                id = SHOW_ALL_ID,
                name = getString(R.string.showOptionsShowAll)
            )
        )

        popupMenu = PopupMenuWithList(
            requireContext(),
            fragmentBinding.showOptionsButton,
            header = menuHeader,
            itemList = menuItemList
        )
        popupMenu.onPopupMenuItemClickListener = this
        fragmentBinding.showOptionsButton.apply {
            setSafeOnClickListener {
                popupMenu.show()
            }
        }
    }

    override fun initBinding(): View {
        fragmentBinding = FragmentHomeBinding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onOpen()
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

        (state as HomeFragmentState).apply {
            if (!isLoading) {
                fragmentBinding.balancePanelBalance.text = tokenAmount.toString()
                viewModel.saveTokensAmount(amount = tokenAmount)

                chipsItemList.forEach {
                    it.isSelected = it.id in viewModel.selectedCategories
                }

                chipsItemUiModelList.clear()
                chipsItemUiModelList.addAll(chipsItemList)
                populateChipsMenu()

                menuItemUiModelList.clear()
                menuItemUiModelList.addAll(menuItemList)
                populateMenuRecyclerView()
            }
        }
    }

    private fun showAddTokensDialog() {
        val dialog = OtusDialogFragment.newInstance(OtusDialogFragment.DialogType.ADD_PROMO)
        dialog.show(requireActivity().supportFragmentManager, OtusDialogFragment.DIALOG_TAG)
        dialog.setPositiveCallback { _, code ->
            viewModel.setPromoCode(code)
        }
    }

    private fun showBottomSheetMenuItemDetail(item: MenuItemUiModel) {
        val itemDetail = viewModel.getMenuItemDetailModel(item.id) ?: return

        val dialog = MenuItemDetailFragment.newInstance(
            item = itemDetail,
            changeFavouriteStatus = { changeItemFavouriteStatus(item) }
        )
        dialog.show(requireActivity().supportFragmentManager, MenuItemDetailFragment.DIALOG_TAG)
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
                changeFavouriteStatus = ::changeItemFavouriteStatus
            )
        )
    }

    private fun updateMenuItem(item: MenuItemUiModel) {
        val index = menuAdapter.items.indexOf(item)
        menuAdapter.notifyItemChanged(index)
    }

    private fun changeItemFavouriteStatus(item: MenuItemUiModel) {
        val index = menuItemUiModelList.indexOf(item)
        item.isInFavourite = !item.isInFavourite
        menuItemUiModelList[index].isInFavourite = item.isInFavourite
        if (getFavouriteModeStatus()) populateMenuRecyclerView() else updateMenuItem(item)
        viewModel.changeItemFavouriteStatus(item)

    }

    private fun onModeChanged() {
        setCorrectModeName()
        populateMenuRecyclerView()
    }

    private fun filterMenuItem(chipItemUiModel: ChipItemUiModel) {
        chipsAdapter.notifyItemChanged(chipItemUiModel.id)
        if (chipItemUiModel.isSelected) {
            viewModel.selectedCategories.add(chipItemUiModel.id)
        } else {
            viewModel.selectedCategories.remove(chipItemUiModel.id)
        }

        populateMenuRecyclerView()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun populateChipsMenu() {
        chipsAdapter.apply {
            items = chipsItemUiModelList as List<RecyclerViewItem>?
            notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun populateMenuRecyclerView() {
        if (menuItemUiModelList.isEmpty()) return
        menuAdapter.apply {
            items = (if (getFavouriteModeStatus()) {
                if (viewModel.selectedCategories.isNotEmpty()) {
                    menuItemUiModelList.filter { it.categoryId in viewModel.selectedCategories && it.isInFavourite }
                } else {
                    menuItemUiModelList.filter { it.isInFavourite }
                }
            } else {
                if (viewModel.selectedCategories.isNotEmpty()) {
                    menuItemUiModelList.filter { it.categoryId in viewModel.selectedCategories }
                } else {
                    menuItemUiModelList
                }
            })
            notifyDataSetChanged()
        }
    }

    override fun onPopupMenuItemClicked(item: PopupUiItem) {
        when (item.id) {
            SHOW_ALL_ID -> {
                viewModel.saveFavouriteModeStatus(isActive = false)

            }
            SHOW_FAVOURITE_ID -> {
                viewModel.saveFavouriteModeStatus(isActive = true)

            }
        }
        setCorrectModeName()
        popupMenu.close()
    }

    private fun setCorrectModeName() {
        fragmentBinding.showOptionsButton.text = getString(
            R.string.showOptionsMode,
            if (getFavouriteModeStatus()) {
                getString(R.string.showOptionsShowFavourite)
            } else {
                getString(R.string.showOptionsShowAll)
            }
        ).lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }

    private fun getFavouriteModeStatus(): Boolean {
        return viewModel.getFavouriteModeStatus()
    }

    companion object {
        const val SHOW_ALL_ID = 0
        const val SHOW_FAVOURITE_ID = 1
    }
}