package com.otuscoursework.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.otuscourcework.network.NetworkRepository
import com.otuscourcework.network.models.*
import com.otuscourcework.utils.toFullIsoDate
import com.otuscoursework.ui.fragments.cart.CartFragmentViewModel
import com.otuscoursework.ui.fragments.cart.ui_model.AddressItemUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class CartFragmentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val networkRepository: NetworkRepository = mock()

    private val viewModel = CartFragmentViewModel(networkRepository)

    @Before
    fun setup() {
        whenever(networkRepository.getUserAddresses()).thenReturn(
            mockedAddressList
        )

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should get correct address`() = runTest {
        withContext(Dispatchers.Default) { viewModel.getUserAddresses() }
        val isEquals = viewModel.getAddressById(testAddress1.id) == testAddress1
        assert(isEquals)
    }


    companion object {

        private val testAddress1 = AddressItemUiModel(
            id = 0,
            displayName = "Адрес 1",
            address = "Домашняя улица, 1"
        )

        private val testAddress2 = AddressItemUiModel(
            id = 0,
            displayName = "Адрес 2",
            address = "Рабочая улица, 2"
        )

        private val userAddress1 = UserDeliveryAddress(
            id = 0,
            name = "Адрес 1",
            address = "Домашняя улица, 1"
        )

        private val userAddress2 = UserDeliveryAddress(
            id = 1,
            name = "Адрес 2",
            address = "Рабочая улица, 2"
        )

        private val mockedAddressList = listOf(userAddress1, userAddress2)
    }
}