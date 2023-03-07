package com.amk.market.ui

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amk.market.di.myModules
import com.amk.market.model.repository.TokenInMemory
import com.amk.market.model.repository.user.UserRepository
import com.amk.market.ui.features.IntroScreen
import com.amk.market.ui.features.cart.CartScreen
import com.amk.market.ui.features.category.CategoryScreen
import com.amk.market.ui.features.main.MainScreen
import com.amk.market.ui.features.product.ProductScreen
import com.amk.market.ui.features.profile.ProfileScreen
import com.amk.market.ui.features.signIn.SignInScreen
import com.amk.market.ui.features.signUp.SignUpScreen
import com.amk.market.ui.theme.BackgroundMain
import com.amk.market.ui.theme.MainAppTheme
import com.amk.market.util.KEY_CATEGORY_ARG
import com.amk.market.util.KEY_PRODUCT_ARG
import com.amk.market.util.MyScreens
import dev.burnoo.cokoin.Koin
import dev.burnoo.cokoin.get
import dev.burnoo.cokoin.navigation.KoinNavHost
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        setContent {

            Koin(appDeclaration = {
                androidContext(this@MainActivity)
                modules(myModules)
            }) {
                MainAppTheme {
                    Surface(
                        color = BackgroundMain,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val userRepository: UserRepository = get()
                        userRepository.loadToken()
                        DuniBazaarUI()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainAppTheme {
        Surface(
            color = BackgroundMain,
            modifier = Modifier.fillMaxSize()
        ) {
            DuniBazaarUI()
        }
    }
}

@Composable
fun DuniBazaarUI() {
    val navController = rememberNavController()
    KoinNavHost(
        navController = navController,
        startDestination = MyScreens.MainScreen.route
    ) {
        composable(MyScreens.MainScreen.route) {
            if (TokenInMemory.token != null) {
                MainScreen()
            } else {
                IntroScreen()
            }
        }

        composable(
            route = MyScreens.ProductScreen.route + "/" + "{$KEY_PRODUCT_ARG}",
            arguments = listOf(navArgument(KEY_PRODUCT_ARG) {
                type = NavType.StringType
            })
        ) {
            // Product Id
            ProductScreen(it.arguments!!.getString(KEY_PRODUCT_ARG, null))
        }

        composable(
            route = MyScreens.CategoryScreen.route + "/" + "{$KEY_CATEGORY_ARG}",
            arguments = listOf(navArgument(KEY_CATEGORY_ARG) {
                type = NavType.StringType
            })
        ) {
            // Category Name
            CategoryScreen(it.arguments!!.getString(KEY_CATEGORY_ARG, null))
        }

        composable(MyScreens.ProfileScreen.route) {
            ProfileScreen()
        }

        composable(MyScreens.CartScreen.route) {
            CartScreen()
        }

        composable(MyScreens.SignUpScreen.route) {
            SignUpScreen()
        }

        composable(MyScreens.SignInScreen.route) {
            SignInScreen()
        }

    }
}