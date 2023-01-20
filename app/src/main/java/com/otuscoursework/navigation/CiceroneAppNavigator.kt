package com.otuscoursework.navigation

import com.otuscoursework.ui.main.MainActivity

object CiceroneAppNavigator {

    private val appRouter = MainActivity.INSTANCE.router

    fun toFragment1() {
        appRouter.navigateTo(Screens.fragment1())
    }

    fun toFragment2() {
        appRouter.navigateTo(Screens.fragment2())
    }

    fun back() {
        appRouter.exit()
    }
}
