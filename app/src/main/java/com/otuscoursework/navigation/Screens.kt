package com.otuscoursework.navigation

import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.otuscoursework.ui.fragment1.Fragment1
import com.otuscoursework.ui.fragment2.Fragment2

object Screens {
    fun fragment1() = FragmentScreen { Fragment1() }
    fun fragment2() = FragmentScreen { Fragment2() }
}