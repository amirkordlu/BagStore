package com.amk.market.di

import android.content.Context
import androidx.room.Room
import com.amk.market.model.db.AppDatabase
import com.amk.market.model.net.createApiService
import com.amk.market.model.repository.cart.CartRepository
import com.amk.market.model.repository.cart.CartRepositoryImpl
import com.amk.market.model.repository.comment.CommentRepository
import com.amk.market.model.repository.comment.CommentRepositoryImpl
import com.amk.market.model.repository.product.ProductRepository
import com.amk.market.model.repository.product.ProductRepositoryImpl
import com.amk.market.model.repository.user.UserRepository
import com.amk.market.model.repository.user.UserRepositoryImpl
import com.amk.market.ui.features.cart.CartViewModel
import com.amk.market.ui.features.category.CategoryViewModel
import com.amk.market.ui.features.main.MainViewModel
import com.amk.market.ui.features.product.ProductViewModel
import com.amk.market.ui.features.profile.ProfileViewModel
import com.amk.market.ui.features.signIn.SignInViewModel
import com.amk.market.ui.features.signUp.SignUpViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModules = module {
    single { androidContext().getSharedPreferences("data", Context.MODE_PRIVATE) }
    single { createApiService() }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app_dataBase.db").build()
    }

    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<ProductRepository> { ProductRepositoryImpl(get(), get<AppDatabase>().productDao()) }
    single<CommentRepository> { CommentRepositoryImpl(get()) }
    single<CartRepository> { CartRepositoryImpl(get(), get()) }

    viewModel { SignUpViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { (isInternetConnected: Boolean) -> MainViewModel(get(), isInternetConnected, get()) }
    viewModel { CategoryViewModel(get()) }
    viewModel { ProductViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { CartViewModel(get(), get()) }
}