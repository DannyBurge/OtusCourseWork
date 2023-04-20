package com.otuscoursework.screen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.otuscoursework.matcher.atPosition
import com.otuscoursework.ui.R

class OrdersScreen : BaseScreen() {
    private val orderRV = R.id.orderRecyclerView

    fun checkOrder(pizza: String, size: String, price: String) {
        waitForView()
        onView(withId(orderRV))
            .check(matches(atPosition(0, hasDescendant(withText("$pizza, $size")))))
        onView(withId(orderRV))
            .check(matches(atPosition(0, hasDescendant(withText("₽ $price")))))
    }

    companion object {
        inline operator fun invoke(block: OrdersScreen.() -> Unit) =
            OrdersScreen().block()
    }
}