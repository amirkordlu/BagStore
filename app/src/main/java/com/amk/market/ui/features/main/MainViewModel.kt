package com.amk.market.ui.features.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.market.model.data.Ads
import com.amk.market.model.data.CheckOut
import com.amk.market.model.data.Product
import com.amk.market.model.repository.cart.CartRepository
import com.amk.market.model.repository.product.ProductRepository
import com.amk.market.util.coroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel(
    private val productRepository: ProductRepository,
    isInternetConnected: Boolean,
    private val cartRepository: CartRepository
) : ViewModel() {

    val dataProducts = mutableStateOf<List<Product>>(listOf())
    val dataAds = mutableStateOf<List<Ads>>(listOf())
    val showProgressBar = mutableStateOf(false)
    val badgeNumber = mutableStateOf(0)
    val showPaymentResultDialog = mutableStateOf(false)
    val checkoutData = mutableStateOf(CheckOut(null, null))

    init {
        refreshAllDataFromNet(isInternetConnected)
    }

    fun getCheckoutData() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val result = cartRepository.checkOut(cartRepository.getOrderId())

            if (result.success!!) {
                checkoutData.value = result
                showPaymentResultDialog.value = true
            }
        }
    }

    fun getPaymentStatus(): Int {
        return cartRepository.getPurchaseStatus()
    }

    fun setPaymentStatus(status: Int) {
        cartRepository.setPurchaseStatus(status)
    }

    private fun refreshAllDataFromNet(isInternetConnected: Boolean) {
        viewModelScope.launch(coroutineExceptionHandler) {
            if (isInternetConnected)
                showProgressBar.value = true
            val newDataProducts =
                async { productRepository.getAllProducts(isInternetConnected) }
            val newDataAds = async { productRepository.getAllAds(isInternetConnected) }
            updateData(newDataProducts.await(), newDataAds.await())

            showProgressBar.value = false
        }

    }

    fun loadBadgeNumber() {
        viewModelScope.launch(coroutineExceptionHandler) {
            badgeNumber.value = cartRepository.getCartSize()
        }

    }

    private fun updateData(products: List<Product>, ads: List<Ads>) {
        dataProducts.value = products
        dataAds.value = ads
    }

}