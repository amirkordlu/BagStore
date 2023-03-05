package com.amk.market.model.repository.product

import com.amk.market.model.data.Ads
import com.amk.market.model.data.Product

interface ProductRepository {

    suspend fun getAllProducts(isInternetConnected: Boolean): List<Product>

    suspend fun getAllAds(isInternetConnected: Boolean): List<Ads>

    suspend fun getProductsByCategory(category: String): List<Product>

    suspend fun getProductById(productId: String): Product
}