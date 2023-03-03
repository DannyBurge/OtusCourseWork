package com.otuscoursework.ui.fragments.home

import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.otuscoursework.arch.recycler.RecyclerViewItem
import com.otuscoursework.databinding.ItemChipBinding
import com.otuscoursework.ui.models.ChipItemUiModel
import com.otuscoursework.utils_and_ext.setSafeOnClickListener

typealias FilterItems = (ChipItemUiModel) -> Unit

object ChipAdapterDelegate {
    fun chipDelegate(filterItems: FilterItems) =
        adapterDelegateViewBinding<ChipItemUiModel, RecyclerViewItem, ItemChipBinding>(
            { layoutInflater, root -> ItemChipBinding.inflate(layoutInflater, root, false) }) {
            bind {
                binding.apply {
                    chipCategoryName.text = item.name
                    root.setSafeOnClickListener {
                        item.isSelected = !item.isSelected
                        filterItems.invoke(item)
                    }
                    selectedIndicator.isVisible = item.isSelected
                }
            }
        }
}