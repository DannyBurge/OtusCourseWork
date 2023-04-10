package com.otuscoursework.ui.fragments.notifications.ui_model

import com.otuscoursework.ui.arch.recycler.RecyclerViewItem

data class NotificationItemUiModel(
    override val id: Int,
    val date: String,
    val amountAdded: Int,
    val orderId: Int,
    ) : RecyclerViewItem
