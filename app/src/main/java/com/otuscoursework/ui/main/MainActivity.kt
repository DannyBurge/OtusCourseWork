package com.otuscoursework.ui.main

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.otuscoursework.R
import com.otuscoursework.databinding.ActivityMainBinding
import com.otuscoursework.di.components.ActivityComponent
import com.otuscoursework.di.components.DaggerActivityComponent
import com.otuscoursework.navigation.CiceroneAppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Navigation
    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var ciceroneAppNavigator: CiceroneAppNavigator

    val activityComponent: ActivityComponent = DaggerActivityComponent.builder().build()

    private val navigator = AppNavigator(this, R.id.mainActivityFragmentContainer)

    private lateinit var activityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent.inject(this)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)

        updateBorderPadding()
        setContentView(activityBinding.root)
        initProgressBar()
        ciceroneAppNavigator.toHomeScreen()
    }

    override fun onResume() {
        super.onResume()
        INSTANCE = this
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    private fun updateBorderPadding() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(activityBinding.mainActivityFragmentContainer) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())
            view.updatePadding(0, insets.top, 0, 0)
            WindowInsetsCompat.CONSUMED
        }
    }

    private lateinit var rotateAnimation: RotateAnimation
    private fun initProgressBar() {
        rotateAnimation = RotateAnimation(
            0f,
            359f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotateAnimation.duration = 5000
        rotateAnimation.interpolator = LinearInterpolator()
        activityBinding.progress.animation = rotateAnimation

    }

    fun showLoading() {
        activityBinding.progressBarContainer.isVisible = true
        rotateAnimation.start()
    }

    fun hideLoading() {
        activityBinding.progressBarContainer.isVisible = false
        rotateAnimation.cancel()
//        activityBinding.progress.sto(rotateAnimation)
    }

    fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        lateinit var INSTANCE: MainActivity
    }
}