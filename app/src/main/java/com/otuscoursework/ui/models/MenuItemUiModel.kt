package com.otuscoursework.ui.models

import android.os.Parcelable
import com.otuscoursework.arch.recycler.RecyclerViewItem
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class MenuItemUiModel(
    override val id: Int,
    val subItemIds: List<Int>,
    val price: Int,
    val name: String,
    val categoryId: Int,
    val picture: String,
    val description: String,
    var isInFavourite: Boolean,
) : RecyclerViewItem, Parcelable, Serializable
