package com.otuscoursework.ui.fragments.home

import androidx.core.view.isVisible
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.otuscoursework.R
import com.otuscoursework.arch.recycler.RecyclerViewItem
import com.otuscoursework.databinding.ItemMenuBinding
import com.otuscoursework.ui.models.MenuItemUiModel
import com.otuscoursework.utils_and_ext.setSafeOnClickListener
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation

typealias ShowDetails = (MenuItemUiModel) -> Unit
typealias ChangeFavouriteStatus = (MenuItemUiModel) -> Unit

object MenuAdapterDelegate {
    fun menuDelegate(
        showDetails: ShowDetails,
        changeFavouriteStatus: ChangeFavouriteStatus
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
}