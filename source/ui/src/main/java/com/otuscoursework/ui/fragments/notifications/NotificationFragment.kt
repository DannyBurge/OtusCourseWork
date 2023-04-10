package com.otuscoursework.ui.fragments.notifications

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.otuscourcework.utils.setSafeOnClickListener
import com.otuscoursework.ui.arch.BaseFragment
import com.otuscoursework.ui.arch.BaseState
import com.otuscoursework.ui.arch.recycler.BaseDelegationAdapter
import com.otuscoursework.ui.databinding.FragmentNotificationsBinding
import com.otuscoursework.ui.views.badge_button.ButtonType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<NotificationFragmentViewModel>() {
    override val viewModel: NotificationFragmentViewModel by viewModels()

    override lateinit var fragmentBinding: FragmentNotificationsBinding

    private val notificationAdapter = createNotificationAdapter()

    override fun initBinding(): View {
        fragmentBinding = FragmentNotificationsBinding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.onOpen()
    }

    override fun initViews() {
        fragmentBinding.backButton.apply {
            setButtonType(ButtonType.BACK)
            setSafeOnClickListener {
                ciceroneAppNavigator.back()
            }
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        fragmentBinding.apply {
            notificationRecyclerView.apply {
                adapter = notificationAdapter
                layoutManager = LinearLayoutManager(requireContext()).also {
                    it.orientation = LinearLayoutManager.VERTICAL
                }
            }
        }
    }

    override fun renderState(state: BaseState) {
        super.renderState(state)

        (state as NotificationFragmentState).apply {
            notificationAdapter.apply {
                items = balanceList
                notifyDataSetChanged()
            }
        }
    }

    private fun createNotificationAdapter(): BaseDelegationAdapter {
        return BaseDelegationAdapter(NotificationAdapterDelegate.notificationDelegate(::navigateToOrder))
    }

    private fun navigateToOrder(id: Int) {
        ciceroneAppNavigator.toOrdersScreen(orderId = id)
    }
}