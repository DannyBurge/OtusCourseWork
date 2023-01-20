package com.otuscoursework.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.otuscoursework.R
import com.otuscoursework.databinding.ActivityMainBinding
import com.otuscoursework.navigation.CiceroneAppNavigator

class MainActivity : AppCompatActivity() {

    // Navigation
    lateinit var router: Router
    private lateinit var navigatorHolder: NavigatorHolder
    private val navigator = AppNavigator(this, R.id.mainActivityFragmentContainer)

    private lateinit var activityBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigation()

        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        CiceroneAppNavigator.toFragment1()
    }

    private fun initNavigation() {
        val cicerone = Cicerone.create()
        router = cicerone.router
        navigatorHolder = cicerone.getNavigatorHolder()
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
        activityBinding.mainActivityLoadingView.visibility = View.VISIBLE
    }

    fun hideLoading() {
        activityBinding.mainActivityLoadingView.visibility = View.GONE
    }

    companion object {
        lateinit var INSTANCE: MainActivity
    }
}