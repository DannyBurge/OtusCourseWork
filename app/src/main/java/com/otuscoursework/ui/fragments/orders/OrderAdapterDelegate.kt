package com.otuscoursework.ui.fragments.orders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.otuscoursework.R
import com.otuscoursework.arch.recycler.BaseDelegationAdapter
import com.otuscoursework.arch.recycler.RecyclerViewItem
import com.otuscoursework.databinding.ItemOrderBinding
import com.otuscoursework.ui.fragments.cart.CartAdapterDelegate
import com.otuscoursework.ui.models.OrderItemUiModel
import com.otuscoursework.ui.models.TokenCartItemUiModel
import com.otuscoursework.utils_and_ext.setSafeOnClickListener

object OrderAdapterDelegate {
    fun orderDelegate() =
        adapterDelegateViewBinding<OrderItemUiModel, RecyclerViewItem, ItemOrderBinding>(
            { layoutInflater, root -> ItemOrderBinding.inflate(layoutInflater, root, false) }) {
            bind {
                val orderPositionAdapter = BaseDelegationAdapter(
                    CartAdapterDelegate.cartDelegate(),
                    CartAdapterDelegate.tokenDelegate()
                )

                binding.apply {
                    orderDate.apply {
                        text = if (item.isActive) {
                            setTextColor(resources.getColor(R.color.coral_200))
                            context.getString(R.string.currentOrder)
                        } else {
                            item.date
                        }
                    }
                    price.text = getString(R.string.priceTagAccurate, item.totalPrice)

                    itemsRecyclerView.apply {
                        isVisible = item.isOpen
                        adapter = orderPositionAdapter
                        layoutManager = LinearLayoutManager(context).also {
                            it.orientation = LinearLayoutManager.VERTICAL
                        }
                    }
                    orderPositionAdapter.apply {
                        items = item.orderCheckItemList + TokenCartItemUiModel(-1, item.tokensAdded)
                        notifyDataSetChanged()
                    }

                    root.setSafeOnClickListener {
                        item.isOpen = !item.isOpen
                        itemsRecyclerView.isVisible = item.isOpen
                    }
                }
            }
        }
}