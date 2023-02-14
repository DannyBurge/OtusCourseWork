package com.otuscoursework.ui.fragments.home

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.otuscoursework.arch.recycler.RecyclerViewItem
import com.otuscoursework.ui.models.ChipItemUiModel
import com.otuscoursework.ui.models.MenuItemUiModel

typealias AddToFavourite = () -> Unit
typealias AddToCart = () -> Unit
typealias FilterItems = () -> Unit

object HomeAdapterDelegate {

//    fun menuDelegate(addToFavourite: AddToFavourite, addToCart: AddToCart) =
//        adapterDelegateViewBinding<MenuItemUiModel, RecyclerViewItem, ItemDiaryBinding>(
//            { layoutInflater, root -> ItemDiaryBinding.inflate(layoutInflater, root, false) }) {
//            bind {
//
//            }
//        }
//
//    fun chipDelegate(filterItems: FilterItems) =
//        adapterDelegateViewBinding<ChipItemUiModel, RecyclerViewItem, ItemDiaryBinding>(
//            { layoutInflater, root -> ItemDiaryBinding.inflate(layoutInflater, root, false) }) {
//            bind {
//
//            }
//        }
}