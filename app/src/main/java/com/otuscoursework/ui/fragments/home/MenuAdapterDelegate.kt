package com.otuscoursework.ui.fragments.home

import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.otuscoursework.arch.recycler.RecyclerViewItem
import com.otuscoursework.databinding.ItemMenuBinding
import com.otuscoursework.ui.models.MenuItemUiModel
import com.otuscoursework.utils_and_ext.setSafeOnClickListener

typealias AddToCart = (MenuItemUiModel) -> Unit
typealias RemoveFromCart = (MenuItemUiModel) -> Unit

typealias ChangeFavouriteStatus = (MenuItemUiModel) -> Unit

object MenuAdapterDelegate {
    fun menuDelegate(
        addToCart: AddToCart,
        removeFromCart: RemoveFromCart,
        changeFavouriteStatus: ChangeFavouriteStatus
    ) = adapterDelegateViewBinding<MenuItemUiModel, RecyclerViewItem, ItemMenuBinding>(
        { layoutInflater, root -> ItemMenuBinding.inflate(layoutInflater, root, false) }) {
        bind {
            binding.apply {
                itemName.text = item.name
                itemPrice.text = item.sizes[0].price.toString()
                favouriteButton.setSafeOnClickListener {
                    item.isInFavourite = !item.isInFavourite
                    changeFavouriteStatus.invoke(item)
                }
                favouriteIndicator.isVisible = item.isInFavourite

                binding.expandableButton.apply {
                    setCounter(item.amountInCart)
                    setOnMinusButtonCallback {
                        removeFromCart.invoke(item)
                    }
                    setOnPlusButtonCallback {
                        addToCart.invoke(item)
                    }
                }
            }
        }
    }
}