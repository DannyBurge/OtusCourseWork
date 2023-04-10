package com.otuscoursework.ui.fragments.cart

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.otuscourcework.network.models.UserDeliveryAddress
import com.otuscourcework.utils.enable
import com.otuscourcework.utils.setSafeOnClickListener
import com.otuscoursework.resource.R
import com.otuscoursework.ui.arch.BaseFragment
import com.otuscoursework.ui.databinding.FragmentCartBinding
import com.otuscoursework.ui.fragments.cart.ui_model.CartItemUiModel
import com.otuscoursework.ui.navigation.CiceroneAppNavigator
import com.otuscoursework.ui.arch.BaseState
import com.otuscoursework.ui.views.badge_button.ButtonType
import com.otuscoursework.ui.views.dialog.OtusDialogFragment
import com.otuscoursework.ui.views.popup.PopupMenuWithList
import com.otuscoursework.ui.views.popup.ui_model.AddPopupUiItem
import com.otuscoursework.ui.views.popup.ui_model.PopupUiItem
import com.otuscoursework.ui.views.size_changer.SizeChanger
import com.otuscoursework.ui.arch.recycler.BaseDelegationAdapter
import com.otuscoursework.ui.arch.recycler.RecyclerViewItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartFragment : BaseFragment<CartFragmentViewModel>(),
    PopupMenuWithList.OnPopupMenuItemClickListener,
    PopupMenuWithList.OnAddPopupMenuItemClickListener {

    override val viewModel: CartFragmentViewModel by viewModels()
    override lateinit var fragmentBinding: FragmentCartBinding

    private val cartAdapter = createCartAdapter()

    private lateinit var popupMenu: PopupMenuWithList

    private fun createCartAdapter(): BaseDelegationAdapter {
        return BaseDelegationAdapter(CartAdapterDelegate.cartDelegate())
    }


    override fun initBinding(): View {
        fragmentBinding = FragmentCartBinding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getUserAddresses()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCartItems()
    }

    override fun initViews() {
        initButtons()
        initRecyclerView()
        initTokenUser()

        fragmentBinding.price.text = getString(
            R.string.priceTagAccurate,
            viewModel.getTotalPrice()
        )
    }

    private fun initPopupMenu() {
        val menuHeader = getString(R.string.address_options_title)
        val menuItemList: MutableList<RecyclerViewItem> = mutableListOf()
        menuItemList.addAll(
            viewModel.viewModelState.addressItemList.map {
                PopupUiItem(id = it.id, name = it.displayName)
            }
        )
        menuItemList.add(
            AddPopupUiItem(id = ADD_NEW_ADDRESS, name = getString(R.string.add_new_address))
        )

        popupMenu = PopupMenuWithList(
            requireContext(),
            fragmentBinding.addressCurrent,
            header = menuHeader,
            itemList = menuItemList
        )
        popupMenu.onPopupMenuItemClickListener = this
        popupMenu.onAddPopupMenuItemClickListener = this
        fragmentBinding.addressCurrent.apply {
            setSafeOnClickListener {
                popupMenu.show()
            }
        }
    }

    private fun initTokenUser() {
        fragmentBinding.tokenUser.apply {
            setItemNames(
                listOf(
                    resources.getString(R.string.tokenUserRemove, getMaxDiscount()),
                    resources.getString(R.string.tokenUserAdd, getMaxTokensAddition()),
                )
            )
            blockItem(SizeChanger.INDEX_LEFT, getMaxDiscount() != 0)
            setCheckedItem(SizeChanger.INDEX_RIGHT)
            setOnCheckedChangeListener {
                setPayButtonTextWithIndex(it)
                viewModel.tokensSpendOnOrder = if (it == SizeChanger.INDEX_RIGHT) {
                    getMaxTokensAddition()
                } else {
                    -getMaxDiscount()
                }
            }
        }
    }

    private fun setPayButtonTextWithIndex(index: Int) {
        fragmentBinding.payButton.text = getString(
            R.string.pay,
            when (index) {
                SizeChanger.INDEX_LEFT -> getTotalPrice() - getMaxDiscount()
                SizeChanger.INDEX_RIGHT -> getTotalPrice()
                else -> getTotalPrice()
            }
        )
    }

    private fun getTotalPrice(): Int {
        return viewModel.getTotalPrice()
    }

    private fun getMaxDiscount(): Int {
        return viewModel.getMaxDiscount()
    }

    private fun getMaxTokensAddition(): Int {
        return viewModel.getMaxTokensAddition()
    }

    private fun initButtons() {
        fragmentBinding.apply {
            backButton.apply {
                setButtonType(ButtonType.BACK)
                setSafeOnClickListener {
                    ciceroneAppNavigator.back()
                }
            }
            clearButton.apply {
                setButtonType(ButtonType.TRASH)
                setSafeOnClickListener {
                    showClearCartDialog()
                    fragmentBinding.tokenUser.setCheckedItem(SizeChanger.INDEX_RIGHT)
                }
            }
            payButton.apply {
                setPayButtonTextWithIndex(SizeChanger.INDEX_RIGHT)
                setSafeOnClickListener {
                    viewModel.sendOrder()
                }
            }
        }
    }

    private fun showClearCartDialog() {
        val dialog = OtusDialogFragment.newInstance(OtusDialogFragment.DialogType.CLEAR_CART)
        dialog.show(requireActivity().supportFragmentManager, OtusDialogFragment.DIALOG_TAG)
        dialog.setPositiveCallback { _, _ ->
            viewModel.clearCart()
        }
    }

    private fun initRecyclerView() {
        fragmentBinding.apply {
            cartRecyclerView.apply {
                adapter = cartAdapter
                layoutManager = LinearLayoutManager(requireContext()).also {
                    it.orientation = LinearLayoutManager.VERTICAL
                }
            }
        }
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

        (state as CartFragmentState).apply {
            if (sentOrder != null) {
                ciceroneAppNavigator.toOrdersScreen(sentOrder!!)
                sentOrder = null
                return
            }

            populateCartList(cartItemList)
            fragmentBinding.apply {
                cartListContainer.isVisible = cartItemList.isNotEmpty()
                payButton.apply {
                    enable(cartItemList.isNotEmpty())
                    setPayButtonTextWithIndex(SizeChanger.INDEX_RIGHT)
                }
                tokenUser.apply {
                    setItemNames(
                        listOf(
                            resources.getString(R.string.tokenUserRemove, getMaxDiscount()),
                            resources.getString(R.string.tokenUserAdd, getMaxTokensAddition()),
                        )
                    )
                    blockItem(SizeChanger.INDEX_LEFT, getMaxDiscount() != 0)
                }
            }
            initPopupMenu()
        }
    }

    private fun populateCartList(cartItemModelList: List<CartItemUiModel>) {
        cartAdapter.items = cartItemModelList as List<RecyclerViewItem>
        cartAdapter.notifyDataSetChanged()
    }

    override fun onPopupMenuItemClicked(item: PopupUiItem) {
        val address = viewModel.getAddressById(item.id)
        address?.let {
            viewModel.changeCurrentAddress(it)
            fragmentBinding.addressCurrent.text = "${it.displayName}, ${it.address}"
        }
        popupMenu.close()
    }

    override fun onAddPopupMenuItemClicked(item: AddPopupUiItem) {
        showAddAddressDialog()
        popupMenu.close()
    }

    private fun showAddAddressDialog() {
        val dialog = OtusDialogFragment.newInstance(OtusDialogFragment.DialogType.ADD_ADDRESS)
        dialog.show(requireActivity().supportFragmentManager, OtusDialogFragment.DIALOG_TAG)
        dialog.setPositiveCallback { name, value ->
            viewModel.sendNewAddress(
                address = UserDeliveryAddress(
                    id = null,
                    name = name,
                    address = value
                )
            )
            fragmentBinding.addressCurrent.text = "$name, $value"
        }
    }

    companion object {
        private const val ADD_NEW_ADDRESS = -1
    }
}