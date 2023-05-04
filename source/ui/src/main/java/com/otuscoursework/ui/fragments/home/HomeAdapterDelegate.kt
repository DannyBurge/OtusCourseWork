package com.otuscoursework.ui.fragments.home

import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.otuscourcework.utils.setSafeOnClickListener
import com.otuscoursework.resource.R
import com.otuscoursework.ui.arch.recycler.RecyclerViewItem
import com.otuscoursework.ui.databinding.ItemChipBinding
import com.otuscoursework.ui.databinding.ItemMenuBinding
import com.otuscoursework.ui.fragments.home.ui_model.ChipItemUiModel
import com.otuscoursework.ui.fragments.home.ui_model.MenuItemUiModel
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

object HomeAdapterDelegate {
    fun menuDelegate(
        showDetails: (MenuItemUiModel) -> Unit,
        changeFavouriteStatus: (MenuItemUiModel) -> Unit
    ) = adapterDelegateViewBinding<MenuItemUiModel, RecyclerViewItem, ItemMenuBinding>(
        { layoutInflater, root -> ItemMenuBinding.inflate(layoutInflater, root, false) }) {
        bind {
            binding.apply {
                Picasso.get().load(item.picture)
                    .fit()
                    .transform(
                        RoundedCornersTransformation(
                            context.resources.getDimension(R.dimen.corner_radius).toInt(),
                            0,
                            RoundedCornersTransformation.CornerType.DIAGONAL_FROM_TOP_LEFT
                        )
                    )
                    .into(itemImage)

                root.setSafeOnClickListener {
                    showDetails.invoke(item)
                }

                itemName.text = item.name
                itemPrice.text = getString(R.string.priceTag, item.price)
                favouriteButton.setSafeOnClickListener {
                    changeFavouriteStatus.invoke(item)
                }
                favouriteIndicator.isVisible = item.isInFavourite
            }
        }
    }

    fun chipDelegate(filterItems: (ChipItemUiModel) -> Unit) =
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