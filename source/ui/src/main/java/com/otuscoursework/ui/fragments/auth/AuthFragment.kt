package com.otuscoursework.ui.fragments.auth

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
import androidx.biometric.auth.AuthPromptErrorException
import androidx.biometric.auth.AuthPromptFailureException
import androidx.biometric.auth.AuthPromptHost
import androidx.biometric.auth.Class2BiometricAuthPrompt
import androidx.biometric.auth.Class3BiometricAuthPrompt
import androidx.biometric.auth.authenticate
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.otuscourcework.user_data_keeper.BiometricCipher
import com.otuscourcework.user_data_keeper.Security
import com.otuscourcework.user_data_keeper.UserDataKeeper
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

    private var passIndex = PASS_INDEX_0
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
            oneButton.setOnClickListener { addDigit(DIGIT_1) }
            twoButton.setOnClickListener { addDigit(DIGIT_2) }
            threeButton.setOnClickListener { addDigit(DIGIT_3) }
            fourButton.setOnClickListener { addDigit(DIGIT_4) }
            fiveButton.setOnClickListener { addDigit(DIGIT_5) }
            sixButton.setOnClickListener { addDigit(DIGIT_6) }
            sevenButton.setOnClickListener { addDigit(DIGIT_7) }
            eightButton.setOnClickListener { addDigit(DIGIT_8) }
            nineButton.setOnClickListener { addDigit(DIGIT_9) }
            zeroButton.setOnClickListener { addDigit(DIGIT_0) }
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
                if (passIndex == PASS_INDEX_3) {
                    delay(PASS_REPEAT_DELAY)
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

            if (passIndex == PASS_INDEX_3) {
                passIndex = PASS_INDEX_0
                if (secondTryPass.none { it == null }) {
                    comparePasses()
                } else {
                    delay(PASS_REPEAT_DELAY)
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
        passIndex = PASS_INDEX_0
        firstTryPass[PASS_INDEX_0] = null
        firstTryPass[PASS_INDEX_1] = null
        firstTryPass[PASS_INDEX_2] = null
        firstTryPass[PASS_INDEX_3] = null

        secondTryPass[PASS_INDEX_0] = null
        secondTryPass[PASS_INDEX_1] = null
        secondTryPass[PASS_INDEX_2] = null
        secondTryPass[PASS_INDEX_3] = null
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
            passIndex == PASS_INDEX_0 && viewModel.userDataKeeper.isBiometricAuthActive
    }

    private fun checkPass(): Boolean {
        val correctPass = viewModel.userDataKeeper.hashPassword

        return security.md5(firstTryPass.joinToString(UserDataKeeper.STRING_SEPARATOR)) == correctPass
    }

    private fun colorPassPanel(pass: List<Int?>) {
        fragmentBinding.apply {
            getColorMask(pass).forEachIndexed { index, colorId ->
                val color = viewModel.resHelper.getColor(colorId)
                val drawable = viewModel.resHelper.getDrawable(R.drawable.bg_round_button)
                DrawableCompat.setTint(drawable, color)
                when (index) {
                    PASS_INDEX_0 -> firstPassDigit.background = drawable
                    PASS_INDEX_1 -> secondPassDigit.background = drawable
                    PASS_INDEX_2 -> thirdPassDigit.background = drawable
                    PASS_INDEX_3 -> fourthPassDigit.background = drawable
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
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
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
        private const val DIGIT_0 = 0
        private const val DIGIT_1 = 1
        private const val DIGIT_2 = 2
        private const val DIGIT_3 = 3
        private const val DIGIT_4 = 4
        private const val DIGIT_5 = 5
        private const val DIGIT_6 = 6
        private const val DIGIT_7 = 7
        private const val DIGIT_8 = 8
        private const val DIGIT_9 = 9

        private const val PASS_INDEX_0 = 0
        private const val PASS_INDEX_1 = 1
        private const val PASS_INDEX_2 = 2
        private const val PASS_INDEX_3 = 3
        private const val PASS_REPEAT_DELAY = 500L

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