package com.otuscoursework.ui.fragments.home

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.otuscoursework.arch.recycler.RecyclerViewItem
import com.otuscoursework.databinding.ItemChipBinding
import com.otuscoursework.ui.models.CartCheckItemUiModel
import com.otuscoursework.ui.models.MenuItemUiModel

typealias AddToCart = (CartCheckItemUiModel) -> Unit
typealias RemoveFromCart = (CartCheckItemUiModel) -> Unit

object MenuAdapterDelegate {
    fun menuDelegate(addToCart: AddToCart, removeFromCart: RemoveFromCart) =
        adapterDelegateViewBinding<MenuItemUiModel, RecyclerViewItem, ItemChipBinding>(
            { layoutInflater, root -> ItemChipBinding.inflate(layoutInflater, root, false) }) {
            bind {
                binding.apply {

                }
            }
        }
}