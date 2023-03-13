package com.otuscoursework.ui.fragments.notifications

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.otuscoursework.arch.BaseFragment
import com.otuscoursework.arch.BaseState
import kotlin.properties.Delegates

class NotificationFragment : BaseFragment<NotificationFragmentViewModel>() {
    override val viewModel: NotificationFragmentViewModel by viewModels()

    private var orderId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderId = requireArguments().getInt(ORDER_ID)
    }
    override fun initViews() {
        //TODO("Not yet implemented")

        if (orderId != 0) scrollToOrder()
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

    }

    private fun scrollToOrder() {}

    companion object {
        const val ORDER_ID = "orderId"

        fun newInstance(orderId: Int): NotificationFragment{
            val args = Bundle()
            args.putInt(ORDER_ID, orderId)
            val fragment = NotificationFragment()
            fragment.arguments = args
            return fragment
        }
    }
}