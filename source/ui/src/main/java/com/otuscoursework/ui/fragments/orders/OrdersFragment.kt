package com.otuscoursework.ui.fragments.orders

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.otuscourcework.utils.setSafeOnClickListener
import com.otuscoursework.ui.arch.BaseFragment
import com.otuscoursework.ui.arch.BaseState
import com.otuscoursework.ui.arch.recycler.BaseDelegationAdapter
import com.otuscoursework.ui.databinding.FragmentOrdersBinding
import com.otuscoursework.ui.views.badge_button.ButtonType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment<OrdersFragmentViewModel>() {
    override val viewModel: OrdersFragmentViewModel by viewModels()
    override lateinit var fragmentBinding: FragmentOrdersBinding

    private val orderAdapter = createOrderAdapter()
    private var orderId = EMPTY_ORDER_ID
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderId = requireArguments().getInt(ORDER_ID)
        viewModel.onOpen(orderId)
    }

    override fun initBinding(): View {
        fragmentBinding = FragmentOrdersBinding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun initViews() {
        fragmentBinding.backButton.apply {
            setButtonType(ButtonType.BACK)
            setSafeOnClickListener {
                ciceroneAppNavigator.back()
            }
        }
        initRecyclerView()
    }


    private fun initRecyclerView() {
        fragmentBinding.apply {
            orderRecyclerView.apply {
                adapter = orderAdapter
                layoutManager = LinearLayoutManager(requireContext()).also {
                    it.orientation = LinearLayoutManager.VERTICAL
                }
            }
        }
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

        (state as OrdersFragmentState).apply {
            orderAdapter.apply {
                items = orderList
                notifyDataSetChanged()
                if (orderId > EMPTY_ORDER_ID && orderList.isNotEmpty()) scrollToOrder(orderId)
            }
        }
    }

    private fun scrollToOrder(id: Int) {
        val index = orderAdapter.items.indexOfFirst { it.id == id }
        fragmentBinding.orderRecyclerView.layoutManager!!.scrollToPosition(index)
    }

    private fun createOrderAdapter(): BaseDelegationAdapter {
        return BaseDelegationAdapter(OrderAdapterDelegate.orderDelegate())
    }

    companion object {
        const val EMPTY_ORDER_ID = -1

        const val ORDER_ID = "orderId"

        fun newInstance(orderId: Int): OrdersFragment {
            val args = Bundle()
            args.putInt(ORDER_ID, orderId)
            val fragment = OrdersFragment()
            fragment.arguments = args
            return fragment
        }
    }
}