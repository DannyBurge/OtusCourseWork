package com.otuscoursework.ui.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.otuscourcework.utils.enable
import com.otuscourcework.utils.setSafeOnClickListener
import com.otuscoursework.ui.arch.BaseFragment
import com.otuscoursework.ui.databinding.FragmentSettingsBinding
import com.otuscoursework.ui.views.badge_button.ButtonType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<SettingsFragmentViewModel>() {

    override val viewModel: SettingsFragmentViewModel by viewModels()
    override lateinit var fragmentBinding: FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                ciceroneAppNavigator.toHomeScreen()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun initBinding(): View {
        fragmentBinding = FragmentSettingsBinding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun initViews() {
        fragmentBinding.backButton.apply {
            setButtonType(ButtonType.BACK)
            setSafeOnClickListener {
                ciceroneAppNavigator.toHomeScreen()
            }
        }

        fragmentBinding.apply {
            securityAuthTextView.setSafeOnClickListener {
                securityAuthSwitch.isChecked = !securityAuthSwitch.isChecked
            }

            securityAuthSwitch.apply {
                isChecked = viewModel.userDataKeeper.isAuthActive
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.userDataKeeper.isAuthActive = isChecked
                    if (isChecked) {
                        fingerprintModeSwitch.enable(true)
                        fingerprintModeTextView.enable(true)

                        if (viewModel.userDataKeeper.hashPassword == null) {
                            ciceroneAppNavigator.toAuthScreen(isCreatePasswordMode = true)
                        }
                    } else {
                        viewModel.userDataKeeper.hashPassword = null
                        fingerprintModeSwitch.isChecked = false
                        fingerprintModeSwitch.enable(false)
                        fingerprintModeTextView.enable(false)
                    }
                }
            }

            fingerprintModeTextView.setSafeOnClickListener {
                fingerprintModeSwitch.isChecked = !fingerprintModeSwitch.isChecked
            }

            fingerprintModeSwitch.apply {
                enable(viewModel.userDataKeeper.isAuthActive)
                isChecked = viewModel.userDataKeeper.isBiometricAuthActive
                setOnCheckedChangeListener { _, isChecked ->
                    viewModel.userDataKeeper.isBiometricAuthActive = isChecked
                }
            }
        }
    }
}