package com.otuscoursework.ui.fragments.cart

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.otuscoursework.resource.R
import com.otuscoursework.ui.arch.recycler.RecyclerViewItem
import com.otuscoursework.ui.databinding.ItemCartBinding
import com.otuscoursework.ui.databinding.ItemCartTokenBinding
import com.otuscoursework.ui.fragments.cart.ui_model.CartItemUiModel
import com.otuscoursework.ui.fragments.cart.ui_model.TokenCartItemUiModel

object CartAdapterDelegate {
    fun cartDelegate() =
        adapterDelegateViewBinding<CartItemUiModel, RecyclerViewItem, ItemCartBinding>(
            { layoutInflater, root -> ItemCartBinding.inflate(layoutInflater, root, false) }
        ) {
            bind {
                binding.apply {
                    cartItemName.text = item.name
                    cartItemAmountPrice.text =
                        getString(R.string.amountAndPrice, item.amount, item.price)
                }
            }
        }

    fun tokenDelegate() =
        adapterDelegateViewBinding<TokenCartItemUiModel, RecyclerViewItem, ItemCartTokenBinding>(
    { layoutInflater, root -> ItemCartTokenBinding.inflate(layoutInflater, root, false) })
    {
        bind {
            binding.apply {
                cartItemAmountPrice.apply {
                    text = if (item.amount > 0) {
                        setTextColor(resources.getColor(R.color.gold_200))
                        "+${item.amount}"
                    } else {
                        setTextColor(resources.getColor(R.color.coral_200))
                        item.amount.toString()
                    }
                }
            }
        }
    }
}