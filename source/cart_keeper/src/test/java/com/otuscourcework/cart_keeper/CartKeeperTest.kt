package com.otuscourcework.cart_keeper

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class CartKeeperTest {

    private val cartKeeper = CartKeeper()

    @Before
    fun addListOf3ItemsToCart() {
        runBlocking {
            listOf3CartItems.forEach {
                cartKeeper.addItemToCart(it)
            }
        }
    }

    @Test
    fun `correct numbers when deleting item`() {
        runBlocking {
            cartKeeper.removeItemFromCart(item3.id)
        }
        val correctAmount = 2
        val testedAmount = cartKeeper.getCartContent().values.size

        assertEquals(correctAmount, testedAmount)
    }

    @Test
    fun `correct items when deleting item`() {
        runBlocking {
            cartKeeper.removeItemFromCart(item3.id)
        }

        val isEqual =
            cartKeeper.getCartContent().values.sortedBy { it.id } == listOf2CartItems.sortedBy { it.id }

        assert(isEqual)
    }

    @Test
    fun `correct numbers when adding item`() {
        runBlocking {
            cartKeeper.addItemToCart(item4)
        }
        val correctAmount = 4
        val testedAmount = cartKeeper.getCartContent().values.size

        assertEquals(correctAmount, testedAmount)
    }

    @Test
    fun `correct items when adding item`() {
        runBlocking {
            cartKeeper.addItemToCart(item4)
        }
        val isEqual =
            cartKeeper.getCartContent().values.sortedBy { it.id } == listOf4CartItems.sortedBy { it.id }

        assert(isEqual)
    }

    @Test
    fun `correct clearing items`() {
        runBlocking {
            cartKeeper.clearCart()
        }
        val correctAmount = 0
        val testedAmount = cartKeeper.getCartContent().values.size

        assertEquals(correctAmount, testedAmount)
    }

    @Test
    fun `correct total price`() {
        val correctAmount = totalPriceOfListOf3CartItems
        val testedAmount = cartKeeper.getTotalPrice()

        assertEquals(correctAmount, testedAmount)
    }

    companion object {
        private val item1 = CartItemModel(
            groupId = 0,
            id = 0,
            name = "Пицца 1",
            subName = "20см",
            price = 200,
            amount = 1,
        )
        private val item2 = CartItemModel(
            groupId = 1,
            id = 5,
            name = "Пицца 2",
            subName = "30см",
            price = 300,
            amount = 1,
        )
        private val item3 = CartItemModel(
            groupId = 2,
            id = 9,
            name = "Пицца 3",
            subName = "40см",
            price = 400,
            amount = 1,
        )
        private val item4 = CartItemModel(
            groupId = 3,
            id = 10,
            name = "Пицца 4",
            subName = "20см",
            price = 200,
            amount = 1,
        )

        private val listOf2CartItems = listOf(item1, item2)
        private val listOf3CartItems = listOf(item1, item2, item3)
        private val listOf4CartItems = listOf(item1, item2, item3, item4)

        private val totalPriceOfListOf3CartItems = listOf3CartItems.sumOf { it.price }
    }
}