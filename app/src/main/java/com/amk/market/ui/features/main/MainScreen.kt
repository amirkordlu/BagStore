package com.amk.market.ui.features.main

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.amk.market.model.data.Ads
import com.amk.market.model.data.Product
import com.amk.market.ui.theme.*
import com.amk.market.util.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dev.burnoo.cokoin.navigation.getNavController
import dev.burnoo.cokoin.navigation.getNavViewModel
import org.koin.core.parameter.parametersOf

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = BackgroundMain
        ) {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    val uiController = rememberSystemUiController()
    SideEffect { uiController.setStatusBarColor(Color.White) }
    val context = LocalContext.current

    val viewModel =
        getNavViewModel<MainViewModel>(parameters = { parametersOf(NetworkChecker(context).isInternetConnected) })
    val navigation = getNavController()

    if (NetworkChecker(context).isInternetConnected) {
        viewModel.loadBadgeNumber()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp)
    ) {

        if (viewModel.showProgressBar.value) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = Blue
            )
        }

        TopToolbar(
            badgeNumber = viewModel.badgeNumber.value,
            onCartClicked = {
                if (NetworkChecker(context).isInternetConnected) {
                    navigation.navigate(MyScreens.CartScreen.route)
                } else {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                }
            },
            onProfileClicked = {
                navigation.navigate(MyScreens.ProfileScreen.route)
            })

        CategoryBar(CATEGORY) {
            navigation.navigate(MyScreens.CategoryScreen.route + "/" + it)
        }

        val productDataState = viewModel.dataProducts
        val adsDataState = viewModel.dataAds
        ProductSubjectList(TAGS, productDataState.value, adsDataState.value) {
            navigation.navigate(MyScreens.ProductScreen.route + "/" + it)
        }
    }
}

//-----------------------------------------------

@Composable
fun ProductSubjectList(
    tags: List<String>,
    products: List<Product>,
    ads: List<Ads>,
    onProductClicked: (String) -> Unit
) {
    val context = LocalContext.current

    if (products.isNotEmpty()) {
        Column {
            tags.forEachIndexed { it, _ ->
                val withTagData = products.filter { product ->
                    product.tags == tags[it]
                }
                ProductBySubject(tags[it], withTagData.shuffled(), onProductClicked)

                if (ads.size >= 2) {
                    if (it == 1 || it == 2) {
                        BigBannerAds(ads[it - 1], onProductClicked)
                    }
                }
            }
        }
    }
}


@Composable
fun TopToolbar(badgeNumber: Int, onCartClicked: () -> Unit, onProfileClicked: () -> Unit) {
    TopAppBar(
        elevation = 0.dp,
        backgroundColor = Color.White,
        title = { Text(text = "Bag Market") },
        actions = {

            IconButton(
                onClick = { onCartClicked.invoke() }
            ) {
                if (badgeNumber == 0) {
                    Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                } else {
                    BadgedBox(badge = { Badge { Text(badgeNumber.toString()) } }) {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                    }
                }
            }

            IconButton(onClick = { onProfileClicked.invoke() }) {
                Icon(Icons.Default.Person, null)
            }
        }
    )
}

//-----------------------------------------------

@Composable
fun CategoryBar(categoryList: List<Pair<String, Int>>, onCategoryClicked: (String) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(categoryList.size) {
            CategoryItem(categoryList[it], onCategoryClicked)
        }
    }
}

@Composable
fun CategoryItem(subject: Pair<String, Int>, onCategoryClicked: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { onCategoryClicked.invoke(subject.first) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = Shapes.medium,
            color = CardViewBackground
        ) {
            Image(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = subject.second),
                contentDescription = null
            )
        }
        Text(
            text = subject.first,
            modifier = Modifier.padding(top = 4.dp),
            style = TextStyle(color = Color.Gray)
        )
    }
}

//-----------------------------------------------

@Composable
fun ProductBySubject(subject: String, data: List<Product>, onProductClicked: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(top = 32.dp)
    ) {
        Text(
            text = subject,
            modifier = Modifier.padding(start = 16.dp),
            style = MaterialTheme.typography.h6
        )

        ProductBar(data, onProductClicked)
    }
}

@Composable
fun ProductBar(data: List<Product>, onProductClicked: (String) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(top = 16.dp),
        contentPadding = PaddingValues(end = 16.dp)
    ) {
        items(data.size) {
            ProductItem(data[it], onProductClicked)
        }
    }
}

@Composable
fun ProductItem(product: Product, onProductClicked: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .clickable { onProductClicked.invoke(product.productId) },
        elevation = 4.dp,
        shape = Shapes.medium
    ) {
        Column {
            AsyncImage(
                model = product.imgUrl,
                contentDescription = null,
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = product.name,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                )

                Text(
                    text = stylePrice(product.price),
                    style = TextStyle(
                        fontSize = 14.sp
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = "${product.soldItem} sold",
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                )
            }
        }
    }
}

//-----------------------------------------------

@Composable
fun BigBannerAds(ad: Ads, onProductClicked: (String) -> Unit) {
    AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(top = 32.dp, start = 16.dp, end = 16.dp)
            .clip(Shapes.medium)
            .clickable { onProductClicked.invoke(ad.productId) },
        model = ad.imageURL,
        contentDescription = null,
        contentScale = ContentScale.Crop
    )
}
