package com.otuscoursework.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.*
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.otuscoursework.ui.R
import com.otuscoursework.ui.databinding.ActivityMainBinding
import com.otuscoursework.ui.navigation.CiceroneAppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // Navigation
    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var ciceroneAppNavigator: CiceroneAppNavigator


    private val navigator = AppNavigator(this, R.id.mainActivityFragmentContainer)

    private lateinit var activityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)

        updateBorderPadding()
        setContentView(activityBinding.root)
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

    companion object {
        lateinit var INSTANCE: MainActivity
    }
}