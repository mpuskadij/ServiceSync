package hr.foi.air.servicesync.ui.contents

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.tertiaryDark
import com.google.firebase.firestore.GeoPoint
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.backend.FirestoreCompanyDetails
import hr.foi.air.servicesync.business.CompanyDetailsHandler
import hr.foi.air.servicesync.business.FavoritesHandler
import hr.foi.air.servicesync.business.MapProviderManager
import hr.foi.air.servicesync.business.ReviewHandler
import hr.foi.air.servicesync.data.Review
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.ui.components.CompanyDescription
import hr.foi.air.servicesync.ui.components.CompanyImage
import hr.foi.air.servicesync.ui.components.CompanyLocation
import hr.foi.air.servicesync.ui.components.FloatingCard
import hr.foi.air.servicesync.ui.components.ReviewList
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.items.ProvidedServicesListItem
import kotlinx.coroutines.launch


@Composable
fun CompanyDetailsContent(
    modifier: Modifier = Modifier,
    context: Context,
    navController: NavController,
    companyName: String,
    reviewHandler: ReviewHandler = ReviewHandler(),
    favoritesHandler: FavoritesHandler = FavoritesHandler()
) {
    //Favorites
    val coroutineScope = rememberCoroutineScope()
    val isFavorite = remember { mutableStateOf(false) }
    val userId = UserSession.username

    val firestoreCompanyDetails = FirestoreCompanyDetails()
    val reviewHandler = ReviewHandler()

    val companyDescription = remember { mutableStateOf(context.getString(R.string.loading)) }
    val companyCategory = remember { mutableStateOf(context.getString(R.string.loading)) }
    val companyOpeningTime = remember { mutableStateOf("") }
    val companyClosingTime = remember { mutableStateOf("") }
    val companyGeoPoint = remember { mutableStateOf<GeoPoint?>(null) }
    val companyImageUrl = remember { mutableStateOf<String?>(null) }
    val reviews = remember { mutableStateOf<List<Review>>(emptyList()) }
    val services = remember { mutableStateOf<List<String>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(companyName) {
        favoritesHandler.isFavorite(userId, companyName) { favorite ->
            isFavorite.value = favorite
        }
        val handler = CompanyDetailsHandler()
        handler.getCompanyDetails(
            context = context,
            companyName = companyName,
            companyDescription = companyDescription,
            companyCategory = companyCategory,
            companyClosingTime = companyClosingTime,
            companyOpeningTime = companyOpeningTime,
            companyGeoPoint = companyGeoPoint,
            companyImageUrl = companyImageUrl,
            reviews = reviews,
            services = services,
            isLoading = isLoading,
            firestoreCompanyDetails = firestoreCompanyDetails,
            reviewHandler = reviewHandler
        )
    }


    if (isLoading.value) {
        Text(stringResource(R.string.loading_data), modifier = Modifier.padding(16.dp))
    } else {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            val headlineTextStyle = MaterialTheme.typography.headlineMedium

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                CompanyImage(
                    companyName = companyName,
                    imageUrl = companyImageUrl.value,
                    onBackPressed = { navController.popBackStack() }
                )

                Icon(
                    imageVector = if (isFavorite.value) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null,
                    tint = tertiaryDark,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.TopEnd)
                        .padding(0.dp, 20.dp, 20.dp, 0.dp)
                        .clickable {
                            coroutineScope.launch {
                                if (isFavorite.value) {
                                    favoritesHandler.removeFavorite(userId, companyName)
                                } else {
                                    favoritesHandler.addFavorite(userId, companyName)
                                }
                                isFavorite.value = !isFavorite.value
                            }
                        }
                )

            }

            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.company_description),
                        color = isDark(onSurfaceDark, onSurfaceLight),
                        style = headlineTextStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                },
                supportingContent = {
                    CompanyDescription(companyDescription.value)
                }
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.services),
                        color = isDark(onSurfaceDark, onSurfaceLight),
                        style = headlineTextStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )
                },
                supportingContent = {
                    Column {
                        services.value.forEach { service ->
                            FloatingCard { ProvidedServicesListItem(serviceName = service, onServiceClicked = {
                                navController.navigate("company/$companyName/$service")
                            }) }
                        }
                    }
                }
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.working_hours),
                        color = isDark(onSurfaceDark, onSurfaceLight),
                        style = headlineTextStyle,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                },
                supportingContent = {
                    Text(
                        text = "${companyOpeningTime.value} - ${companyClosingTime.value}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )

            if (MapProviderManager.getAllProviders().isNotEmpty()) {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.location),
                            color = isDark(onSurfaceDark, onSurfaceLight),
                            style = headlineTextStyle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )
                    },
                    supportingContent = {
                        companyGeoPoint.value?.let { geoPoint ->
                            Log.d("Firestore", "Supporting content geopoint: $geoPoint")
                            CompanyLocation(geoPoint = geoPoint)
                        } ?: Text("Location data is unavailable.")
                    }

                )
            }

            ListItem(
                headlineContent = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.reviews),
                            color = isDark(onSurfaceDark, onSurfaceLight),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            )
            ReviewList(reviews = reviews.value)
        }
    }
}


@Preview
@Composable
fun CompanyDetailsPreview(){
    CompanyDetailsContent(context = LocalContext.current, navController = rememberNavController(), companyName = "Sample Company")
}
