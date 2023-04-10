package com.otuscoursework.ui.views.popup

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.otuscourcework.utils.setSafeOnClickListener
import com.otuscoursework.ui.arch.recycler.RecyclerViewItem
import com.otuscoursework.ui.databinding.ItemAddPopupBinding
import com.otuscoursework.ui.databinding.ItemPopupBinding
import com.otuscoursework.ui.views.popup.ui_model.AddPopupUiItem
import com.otuscoursework.ui.views.popup.ui_model.PopupUiItem

object PopupAdapterDelegate {
    fun popupItemDelegate(itemClickCallback: (PopupUiItem) -> Unit) =
        adapterDelegateViewBinding<PopupUiItem, RecyclerViewItem, ItemPopupBinding>(
            { layoutInflater, root -> ItemPopupBinding.inflate(layoutInflater, root, false) }) {
            bind {
                binding.apply {
                    popupItemTextView.text = item.name
                    root.setSafeOnClickListener {
                        itemClickCallback.invoke(item)
                    }
                }
            }
        }

    fun addAddressItemDelegate(itemClickCallback: (AddPopupUiItem) -> Unit) =
        adapterDelegateViewBinding<AddPopupUiItem, RecyclerViewItem, ItemAddPopupBinding>(
            { layoutInflater, root -> ItemAddPopupBinding.inflate(layoutInflater, root, false) }) {
            bind {
                binding.apply {
                    popupItemTextView.text = item.name
                    root.setSafeOnClickListener {
                        itemClickCallback.invoke(item)
                    }
                }
            }
        }
}