package com.otuscoursework

import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.otuscoursework.screen.CartScreen
import com.otuscoursework.screen.HomeScreen
import com.otuscoursework.screen.OrdersScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UITests : TestBase() {

    @Test
    fun testCreateOrder() {
        HomeScreen {
            choosePizza(testedPizzaName)
            addPizzaToCart()
            goToCart()
        }
        CartScreen {
            clickPay()
        }
        OrdersScreen {
            checkOrder(testedPizzaName, testedPizzaSize, testedPizzaPrice)
        }
    }

    @Test
    fun testAddToFavourite() {
        HomeScreen {
            choosePizza(testedPizzaName)
            waitForView()
            likePizza()
            pressBack()
            showFavourites()
            checkPizzaInFavourite(testedPizzaName)
        }
    }

    @Test
    fun testChangeCategory() {
        HomeScreen {
            changeCategory(testedCategoryName)
            checkCategoryIsSelected(testedCategoryName)
        }
    }

    companion object {
        private const val testedCategoryName = "Напитки"
        private const val testedPizzaName = "Мясная"
        private const val testedPizzaSize = "30 см"
        private const val testedPizzaPrice = "810"
    }
}