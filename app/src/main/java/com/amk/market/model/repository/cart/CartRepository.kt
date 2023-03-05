package com.amk.market.model.repository.cart

import com.amk.market.model.data.CheckOut
import com.amk.market.model.data.SubmitOrder
import com.amk.market.model.data.UserCartInfo

interface CartRepository {

    suspend fun addToCart(productId: String): Boolean

    suspend fun getCartSize(): Int

    suspend fun removeFromCart(productId: String): Boolean

    suspend fun getUserCartInfo(): UserCartInfo

    suspend fun submitOrder(address: String, postalCode: String): SubmitOrder

    suspend fun checkOut(orderId: String): CheckOut

    fun setOrderId(orderId: String)

    fun getOrderId(): String

    fun setPurchaseStatus(status: Int)

    fun getPurchaseStatus(): Int

}