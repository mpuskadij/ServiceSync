package hr.foi.air.servicesync.ui.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.common.io.Resources
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.FavoritesHandler
import hr.foi.air.servicesync.business.ReviewHandler
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.ui.components.CompanyCard

@Composable
fun FavoriteContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val companies = remember { mutableStateOf(emptyList<Map<String, Any>>()) }
    val isLoading = remember { mutableStateOf(true) }
    val favoriteHandler = remember { FavoritesHandler() }

    LaunchedEffect(Unit) {
        isLoading.value = true

        favoriteHandler.getFavoriteCompanies(UserSession.username) { favoriteCompanies ->
            companies.value = favoriteCompanies
            isLoading.value = false
        }
    }

    if (isLoading.value) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column (
            modifier = Modifier
                .padding(16.dp, 50.dp, 16.dp, 0.dp)
        ){
            if (companies.value.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = stringResource(R.string.no_favorites), style = MaterialTheme.typography.headlineLarge)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(companies.value) { company ->
                        val name = company["name"] as? String ?: "No Name"
                        val category = company["category"] as? String ?: "No Category"
                        val imageUrl = company["pictureURL"] as? String

                        val reviewsHandler = ReviewHandler()
                        val reviewAverages = remember { mutableStateOf(mapOf<String, Double>()) }

                        val averageRating = reviewAverages.value[name] ?: "Loading..."

                        if (!reviewAverages.value.containsKey(name)) {
                            reviewsHandler.fetchAndCalculateReviewAverage(name) { average ->
                                reviewAverages.value += (name to average)
                            }
                        }

                        CompanyCard(
                            companyName = name,
                            companyCategory = category,
                            companyRating = averageRating.toString(),
                            imageUrl = imageUrl,
                            onCardClick = {
                                navController.navigate("company/$name")
                            }
                        )
                    }
                }
            }
        }
    }
}