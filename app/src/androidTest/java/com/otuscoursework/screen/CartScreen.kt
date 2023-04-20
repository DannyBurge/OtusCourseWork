package com.otuscoursework.screen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.otuscoursework.ui.R

class CartScreen : BaseScreen() {
    private val payButton = R.id.payButton

    fun clickPay() {
        waitForView()
        onView(withId(payButton)).perform(click())
    }

    companion object {
        inline operator fun invoke(block: CartScreen.() -> Unit) =
            CartScreen().block()
    }
}