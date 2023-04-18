package com.otuscoursework.ui.fragments.auth

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.auth.*
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.otuscourcework.user_data_keeper.BiometricCipher
import com.otuscourcework.user_data_keeper.UserDataKeeper
import com.otuscourcework.user_data_keeper.Security
import com.otuscoursework.resource.R
import com.otuscoursework.ui.arch.BaseFragment
import com.otuscoursework.ui.databinding.FragmentAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : BaseFragment<AuthFragmentViewModel>() {

    override val viewModel: AuthFragmentViewModel by viewModels()
    override lateinit var fragmentBinding: FragmentAuthBinding

    @Inject
    lateinit var security: Security

    private var isCreatePasswordMode: Boolean = false

    private var passIndex = 0
    private val firstTryPass = mutableListOf<Int?>(null, null, null, null)
    private val secondTryPass = mutableListOf<Int?>(null, null, null, null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        isCreatePasswordMode = requireArguments().getBoolean(IS_CREATE_PASSWORD_MODE, false)
    }

    override fun initBinding(): View {
        fragmentBinding = FragmentAuthBinding.inflate(layoutInflater, null, false)
        return fragmentBinding.root
    }

    override fun initViews() {
        if (isCreatePasswordMode) {
            initCreatePasswordMode()
        } else {
            initAuthMode()
        }
    }

    private fun initCreatePasswordMode() {
        initPassKeyboard()

        fragmentBinding.authTitle.text =
            viewModel.resHelper.getStringById(R.string.auth_title_create_pass)


    }

    private fun initAuthMode() {
        if (viewModel.userDataKeeper.isAuthActive) {
            if (viewModel.userDataKeeper.isBiometricAuthActive) initBiometricPrompt()
            initPassKeyboard()
        } else {
            ciceroneAppNavigator.toHomeScreen()
        }
    }

    override fun onResume() {
        super.onResume()

        clearPass()
        checkFingerprintButtonVisibility()
    }

    private fun initPassKeyboard() {
        colorPassPanel(firstTryPass)
        fragmentBinding.root.isVisible = true
        fragmentBinding.apply {
            oneButton.setOnClickListener { addDigit(1) }
            twoButton.setOnClickListener { addDigit(2) }
            threeButton.setOnClickListener { addDigit(3) }
            fourButton.setOnClickListener { addDigit(4) }
            fiveButton.setOnClickListener { addDigit(5) }
            sixButton.setOnClickListener { addDigit(6) }
            sevenButton.setOnClickListener { addDigit(7) }
            eightButton.setOnClickListener { addDigit(8) }
            nineButton.setOnClickListener { addDigit(9) }
            zeroButton.setOnClickListener { addDigit(0) }
            deleteDigitButton.setOnClickListener { deleteDigit() }

            fingerprintButton.setOnClickListener { initBiometricPrompt() }
        }
    }

    private fun addDigit(digit: Int) {
        if (isCreatePasswordMode) {
            createPassAddDigit(digit)
        } else {
            enterPassAddDigit(digit)
        }
    }

    private fun enterPassAddDigit(digit: Int) {
        lifecycleScope.launch {
            firstTryPass[passIndex] = digit

            if (checkPass()) {
                ciceroneAppNavigator.toHomeScreen()
            } else {
                if (passIndex == 3) {
                    delay(500)
                    clearPass()
                } else {
                    passIndex++
                }
                checkFingerprintButtonVisibility()
            }
            colorPassPanel(firstTryPass)
        }
    }

    private fun createPassAddDigit(digit: Int) {
        lifecycleScope.launch {
            if (firstTryPass[passIndex] == null) {
                firstTryPass[passIndex] = digit
                colorPassPanel(firstTryPass)
            } else {
                secondTryPass[passIndex] = digit
                colorPassPanel(secondTryPass)
            }

            if (passIndex == 3) {
                passIndex = 0
                if (secondTryPass.none { it == null }) {
                    comparePasses()
                } else {
                    delay(500)
                    fragmentBinding.authTitle.text =
                        viewModel.resHelper.getStringById(R.string.auth_title_repeat_pass)
                    colorPassPanel(secondTryPass)
                }
            } else {
                passIndex++
            }
        }
    }

    private fun comparePasses() {
        if (firstTryPass == secondTryPass && firstTryPass.none { it == null }) {
            val hasPass = security.md5(firstTryPass.joinToString(UserDataKeeper.STRING_SEPARATOR))

            viewModel.userDataKeeper.hashPassword = hasPass
            ciceroneAppNavigator.toSettingsScreen()
        } else {
            Toast.makeText(
                requireContext(),
                viewModel.resHelper.getStringById(R.string.create_pass_error),
                Toast.LENGTH_SHORT
            ).show()
            clearPass()
            fragmentBinding.authTitle.text =
                viewModel.resHelper.getStringById(R.string.auth_title_create_pass)
        }
    }

    private fun clearPass() {
        passIndex = 0
        firstTryPass[0] = null
        firstTryPass[1] = null
        firstTryPass[2] = null
        firstTryPass[3] = null

        secondTryPass[0] = null
        secondTryPass[1] = null
        secondTryPass[2] = null
        secondTryPass[3] = null
        colorPassPanel(firstTryPass)
    }

    private fun deleteDigit() {
        passIndex--
        firstTryPass[passIndex] = null
        colorPassPanel(firstTryPass)
        if (!isCreatePasswordMode) checkFingerprintButtonVisibility()
    }

    private fun checkFingerprintButtonVisibility() {
        fragmentBinding.deleteDigitButton.isVisible = passIndex != 0
        fragmentBinding.fingerprintButton.isVisible =
            passIndex == 0 && viewModel.userDataKeeper.isBiometricAuthActive
    }

    private fun checkPass(): Boolean {
        val correctPass = viewModel.userDataKeeper.hashPassword

        return security.md5(firstTryPass.joinToString(UserDataKeeper.STRING_SEPARATOR)) == correctPass
    }

    private fun colorPassPanel(pass: List<Int?>) {
        otusLogger.log(passIndex)
        fragmentBinding.apply {
            getColorMask(pass).forEachIndexed { index, colorId ->
                val color = viewModel.resHelper.getColor(colorId)
                val drawable = viewModel.resHelper.getDrawable(R.drawable.bg_round_button)
                DrawableCompat.setTint(drawable, color)
                when (index) {
                    0 -> firstPassDigit.background = drawable
                    1 -> secondPassDigit.background = drawable
                    2 -> thirdPassDigit.background = drawable
                    3 -> fourthPassDigit.background = drawable
                }
            }
        }
    }

    private fun getColorMask(pass: List<Int?>): List<Int> {
        return pass.map { digit ->
            if (digit == null) {
                R.color.coral_200
            } else {
                R.color.gold_200
            }
        }
    }

    private fun initBiometricPrompt() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val success = BiometricManager
                .from(requireContext())
                .canAuthenticate(BIOMETRIC_WEAK) == BIOMETRIC_SUCCESS

            if (success) {
                val authPrompt = buildClass2BiometricAuthPrompt()

                lifecycleScope.launch {
                    try {
                        authPrompt.authenticate(AuthPromptHost(this@AuthFragment))
                        ciceroneAppNavigator.toHomeScreen()
                    } catch (e: AuthPromptErrorException) {
                        otusLogger.log("AuthPromptError: " + (e.message ?: "no message"))
                    } catch (e: AuthPromptFailureException) {
                        otusLogger.log("AuthPromptFailure: " + (e.message ?: "no message"))
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Biometry not supported", Toast.LENGTH_LONG).show()
            }
        } else {
            val success = BiometricManager
                .from(requireContext())
                .canAuthenticate(BIOMETRIC_STRONG) == BIOMETRIC_SUCCESS

            if (success) {
                val biometricCipher = BiometricCipher(requireContext().applicationContext)
                val encryptor = biometricCipher.getEncryptor()

                val authPrompt = buildClass3BiometricAuthPrompt()

                lifecycleScope.launch {
                    try {
                        authPrompt.authenticate(AuthPromptHost(this@AuthFragment), encryptor)
                        ciceroneAppNavigator.toHomeScreen()
                    } catch (e: AuthPromptErrorException) {
                        otusLogger.log("AuthPromptError: " + (e.message ?: "no message"))
                    } catch (e: AuthPromptFailureException) {
                        otusLogger.log("AuthPromptFailure: " + (e.message ?: "no message"))
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Biometry not supported", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun buildClass2BiometricAuthPrompt(): Class2BiometricAuthPrompt {
        return Class2BiometricAuthPrompt
            .Builder(
                viewModel.resHelper.getStringById(R.string.biometric_login_title),
                viewModel.resHelper.getStringById(R.string.biometric_login_dismiss)
            )
            .setConfirmationRequired(true)
            .build()
    }

    private fun buildClass3BiometricAuthPrompt(): Class3BiometricAuthPrompt {
        return Class3BiometricAuthPrompt
            .Builder(
                viewModel.resHelper.getStringById(R.string.biometric_login_title),
                viewModel.resHelper.getStringById(R.string.biometric_login_dismiss)
            )
            .setConfirmationRequired(true)
            .build()
    }

    companion object {
        private const val IS_CREATE_PASSWORD_MODE = "isCreatePasswordMode"

        fun newInstance(isCreatePasswordMode: Boolean): AuthFragment {
            val args = Bundle()
            args.putBoolean(IS_CREATE_PASSWORD_MODE, isCreatePasswordMode)
            val fragment = AuthFragment()
            fragment.arguments = args
            return fragment
        }
    }
}