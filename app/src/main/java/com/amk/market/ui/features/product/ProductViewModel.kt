package com.amk.market.ui.features.product

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.market.model.data.Comment
import com.amk.market.model.repository.cart.CartRepository
import com.amk.market.model.repository.comment.CommentRepository
import com.amk.market.model.repository.product.ProductRepository
import com.amk.market.util.EMPTY_PRODUCT
import com.amk.market.util.coroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository,
    private val commentRepository: CommentRepository,
    private val cartRepository: CartRepository
) : ViewModel() {
    val thisProduct = mutableStateOf(EMPTY_PRODUCT)
    val comments = mutableStateOf(listOf<Comment>())
    val isAddingProduct = mutableStateOf(false)
    val badgeNumber = mutableStateOf(0)

    fun loadData(productId: String, isInternetConnected: Boolean) {
        loadProductFromCache(productId)

        if (isInternetConnected) {
            loadAllComments(productId)
            loadBadgeNumber()
        }
    }

    private fun loadProductFromCache(productId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            thisProduct.value = productRepository.getProductById(productId)
        }
    }

    private fun loadBadgeNumber() {
        viewModelScope.launch(coroutineExceptionHandler) {
            badgeNumber.value = cartRepository.getCartSize()
        }

    }

    private fun loadAllComments(productId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            comments.value = commentRepository.getAllComments(productId)
        }
    }

    fun addNewComment(productId: String, comment: String, isSuccess: (String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            commentRepository.addNewComment(productId, comment, isSuccess)
            delay(100)
            comments.value = commentRepository.getAllComments(productId)
        }
    }

    fun addProductToCart(productId: String, addingToCartResult: (String) -> Unit) {
        viewModelScope.launch(coroutineExceptionHandler) {
            isAddingProduct.value = true

            val result = cartRepository.addToCart(productId)
            delay(500)

            isAddingProduct.value = false

            if (result) {
                addingToCartResult.invoke("Product added to cart")
            } else {
                addingToCartResult.invoke("Product not added")
            }
        }
    }

}