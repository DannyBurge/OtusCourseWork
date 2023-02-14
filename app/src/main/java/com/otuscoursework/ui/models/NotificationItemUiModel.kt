package com.otuscoursework.ui.models

import com.otuscoursework.arch.recycler.RecyclerViewItem
import java.util.*

data class NotificationItemUiModel(
    override val id: Int,
    val date: Date,
    val orderId: Int
    ) : RecyclerViewItem
