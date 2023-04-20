package com.otuscoursework.screen

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeLeft
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.otuscoursework.ui.R
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf

class HomeScreen : BaseScreen() {
    private val itemName = R.id.itemName
    private val backButton = R.id.backButton
    private val plusButton = R.id.plusButton
    private val toCartText = "Перейти в корзину"
    private val favouriteButton = R.id.favouriteDetailButton
    private val showOptionsButton = R.id.showOptionsButton
    private val popupItemTV = R.id.popupItemTextView
    private val favouritesText = "Избранное"
    private val chipsRV = R.id.chipsRecyclerView
    private val chipCategoryName = R.id.chipCategoryName
    private val selectedIndicator = R.id.selectedIndicator

    fun choosePizza(pizza: String) {
        waitForView()
        onView(
            allOf(
                withId(itemName),
                withText(pizza),
                withParent(withParent(instanceOf(ConstraintLayout::class.java)))
            )
        ).perform(click())
        onView(withId(backButton)).perform(click())
    }

    fun addPizzaToCart() {
        onView(withId(plusButton)).perform(click())
    }

    fun goToCart() {
        onView(withText(toCartText)).perform(click())
    }

    fun likePizza() {
        onView(withId(favouriteButton)).perform(click())
    }

    fun showFavourites() {
        onView(withId(showOptionsButton)).perform(click())
        onView(
            allOf(
                withId(popupItemTV),
                withText(favouritesText),
                isDisplayed()
            )
        ).perform(click())
    }

    fun checkPizzaInFavourite(pizza: String) {
        onView(
            allOf(
                withId(itemName),
                withText(pizza)
            )
        ).check(matches(isDisplayed()))
    }

    fun changeCategory(categoryName: String) {
        while (!onView(withText(categoryName)).elementIsDisplayed()) {
            onView(withId(chipsRV)).perform(swipeLeft())
        }
        onView(
            allOf(
                instanceOf(ConstraintLayout::class.java),
                withChild(
                    allOf(
                        withId(chipCategoryName),
                        withText(categoryName)
                    )
                )
            )
        ).perform(click())
    }

    fun checkCategoryIsSelected(categoryName: String) {
        onView(
            allOf(
                withId(selectedIndicator),
                hasSibling(
                    allOf(
                        withId(chipCategoryName),
                        withText(categoryName)
                    )
                )
            )
        ).check(matches(isDisplayed()))
    }

    private fun ViewInteraction.elementIsDisplayed(): Boolean {
        return try {
            check(matches(isDisplayed()))
            true
        } catch (e: NoMatchingViewException) {
            false
        }
    }

    companion object {
        inline operator fun invoke(block: HomeScreen.() -> Unit) =
            HomeScreen().block()
    }
}