package com.otuscoursework.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.otuscoursework.OtusApplication
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

    fun showLoading() {
        activityBinding.mainActivityLoadingView.apply {
            if (visibility == View.GONE) visibility = View.VISIBLE
        }
    }

    fun hideLoading() {
        activityBinding.mainActivityLoadingView.apply {
            if (visibility == View.VISIBLE) visibility = View.GONE
        }
    }

    fun showError(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    companion object {
        lateinit var INSTANCE: MainActivity
    }
}