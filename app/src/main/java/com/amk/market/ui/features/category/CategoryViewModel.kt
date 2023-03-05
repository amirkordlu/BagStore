package com.amk.market.ui.features.category

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amk.market.model.data.Product
import com.amk.market.model.repository.product.ProductRepository
import com.amk.market.util.coroutineExceptionHandler
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    val dataProducts = mutableStateOf<List<Product>>(listOf())

    fun loadProductByCategory(category: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val dataFromLocal = productRepository.getProductsByCategory(category)
            dataProducts.value = dataFromLocal
        }
    }

}