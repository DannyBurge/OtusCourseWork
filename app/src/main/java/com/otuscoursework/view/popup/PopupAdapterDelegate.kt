package com.otuscoursework.view.popup

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.otuscoursework.arch.recycler.RecyclerViewItem
import com.otuscoursework.databinding.ItemAddPopupBinding
import com.otuscoursework.databinding.ItemPopupBinding
import com.otuscoursework.ui.models.AddPopupUiItem
import com.otuscoursework.ui.models.PopupUiItem
import com.otuscoursework.utils_and_ext.setSafeOnClickListener

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