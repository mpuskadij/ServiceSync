package hr.foi.air.servicesync.ui.contents

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.google.firebase.firestore.GeoPoint
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.backend.FirestoreCompanyDetails
import hr.foi.air.servicesync.business.ReviewHandler
import hr.foi.air.servicesync.data.Review
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.ui.components.CompanyDescription
import hr.foi.air.servicesync.ui.components.CompanyImage
import hr.foi.air.servicesync.ui.components.CompanyLocation
import hr.foi.air.servicesync.ui.components.ReviewList
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.items.ProvidedServicesListItem
import mapproviders.GoogleMapProvider


@Composable
fun CompanyDetailsContent(
    modifier: Modifier = Modifier,
    context: Context,
    navController: NavController,
    companyName: String,
    reviewHandler: ReviewHandler = ReviewHandler()
) {
    val firestoreCompanyDetails = FirestoreCompanyDetails()

    val companyDescription = remember { mutableStateOf(context.getString(R.string.loading)) }
    val companyCategory = remember { mutableStateOf(context.getString(R.string.loading)) }
    val companyWorkingHours = remember { mutableStateOf(0) }
    val companyGeoPoint = remember { mutableStateOf<GeoPoint?>(null) }
    val companyImageUrl = remember { mutableStateOf<String?>(null) }
    val reviews = remember { mutableStateOf<List<Review>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(companyName) {
        firestoreCompanyDetails.loadCompanyDescriptionByName(companyName) { description ->
            companyDescription.value = description ?: context.getString(R.string.no_description)
        }

        firestoreCompanyDetails.loadCompanyCategoryByName(companyName) { category ->
            companyCategory.value = category ?: context.getString(R.string.no_category)
        }

        firestoreCompanyDetails.loadCompanyWorkingHoursByName(companyName) { workingHours ->
            companyWorkingHours.value = workingHours ?: 0
        }

        firestoreCompanyDetails.loadCompanyGeopointByName(companyName) { geoPoint ->
            Log.d("Firestore", "Fetchan geopoint: $geoPoint")
            companyGeoPoint.value = geoPoint
            Log.d("Firestore", "Fetchan geopoint value: ${companyGeoPoint.value}")
        }

        firestoreCompanyDetails.loadCompanyImageUrlByName(companyName) { imageUrl ->
            companyImageUrl.value = imageUrl
        }
        reviewHandler.fetchReviews(companyName) { fetchedReviews ->
            reviews.value = fetchedReviews
            isLoading.value = false
        }

        isLoading.value = false
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

            CompanyImage(
                companyName = companyName,
                imageUrl = companyImageUrl.value,
                onBackPressed = { navController.popBackStack() }
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.company_description),
                        color = isDark(onSurfaceDark, onSurfaceLight),
                        style = headlineTextStyle,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
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
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                    )
                },
                supportingContent = {
                    ProvidedServicesListItem(serviceName =  companyCategory.value, onServiceClicked = {
                        navController.navigate("company/${companyName}/${companyCategory.value}")
                    })
                }
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.working_hours),
                        color = isDark(onSurfaceDark, onSurfaceLight),
                        style = headlineTextStyle,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                },
                supportingContent = {
                    Text("${companyWorkingHours.value}")
                }
            )

            ListItem(
                headlineContent = {
                    Text(
                        text = stringResource(id = R.string.location),
                        color = isDark(onSurfaceDark, onSurfaceLight),
                        style = headlineTextStyle,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                    )
                },
                supportingContent = {
                    companyGeoPoint.value?.let { geoPoint ->
                        Log.d("Firestore", "Supporting content geopoint: $geoPoint")
                        CompanyLocation(geoPoint = geoPoint, mapProvider = GoogleMapProvider())
                    } ?: Text("Location data is unavailable.")
                }

            )

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
                        Button(
                            onClick = {
                                navController.navigate("addReview/$companyName/${UserSession.username}")
                            },
                            modifier = Modifier.padding(start = 16.dp)
                        ) {
                            Text(text = stringResource(R.string.add_review))
                        }
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
