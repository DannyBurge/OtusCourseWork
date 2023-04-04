package com.otuscoursework.ui.fragments.notifications

import com.otuscoursework.arch.BaseState
import com.otuscoursework.ui.models.NotificationItemUiModel

data class NotificationFragmentState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val balanceList: List<NotificationItemUiModel> = emptyList()
) : BaseState