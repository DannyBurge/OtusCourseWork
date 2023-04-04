package com.otuscoursework.ui.fragments.notifications

import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.otuscoursework.R
import com.otuscoursework.arch.recycler.RecyclerViewItem
import com.otuscoursework.databinding.ItemNotificationBinding
import com.otuscoursework.ui.models.NotificationItemUiModel
import com.otuscoursework.utils_and_ext.setSafeOnClickListener

object NotificationAdapterDelegate {
    fun notificationDelegate(navigateToOrder: (Int) -> Unit) =
        adapterDelegateViewBinding<NotificationItemUiModel, RecyclerViewItem, ItemNotificationBinding>(
            { layoutInflater, root ->
                ItemNotificationBinding.inflate(layoutInflater, root, false)
            }) {
            bind {
                binding.apply {
                    date.text = item.date
                    amount.apply {
                        text = if (item.amountAdded > 0) {
                            setTextColor(resources.getColor(R.color.gold_200))
                            "+${item.amountAdded}"
                        } else {
                            setTextColor(resources.getColor(R.color.coral_200))
                            item.amountAdded.toString()
                        }
                    }
                    root.setSafeOnClickListener {
                        navigateToOrder.invoke(item.orderId)
                    }
                }
            }
        }
}