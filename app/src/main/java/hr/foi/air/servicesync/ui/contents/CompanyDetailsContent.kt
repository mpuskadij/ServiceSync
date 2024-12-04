package hr.foi.air.servicesync.ui.contents

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.compose.rememberNavController
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.google.firebase.firestore.GeoPoint
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.backend.FirestoreCompanyDetails
import hr.foi.air.servicesync.backend.FirestoreReviews
import hr.foi.air.servicesync.business.ReviewsHandler
import hr.foi.air.servicesync.data.Review
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.ui.components.CompanyDescription
import hr.foi.air.servicesync.ui.components.CompanyLocation
import hr.foi.air.servicesync.ui.components.CompanyNameAndImage
import hr.foi.air.servicesync.ui.components.ReviewCard
import hr.foi.air.servicesync.ui.components.ReviewList
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.items.ProvidedServicesListItem
import mapproviders.GoogleMapProvider


@Composable
fun CompanyDetailsContent(
    modifier: Modifier = Modifier,
    context: Context
) {
    val firestoreCompanyDetails = FirestoreCompanyDetails()
    val firestoreReviews = FirestoreReviews()
    val navController = rememberNavController()

    val companyName = remember { mutableStateOf("Loading...") }
    val companyDescription = remember { mutableStateOf("Loading...") }
    val companyCategory = remember { mutableStateOf("Loading...") }
    val companyWorkingHours = remember { mutableStateOf(0) }
    val companyGeoPoint = remember { mutableStateOf(GeoPoint(0.0, 0.0)) }
    val reviews = remember { mutableStateOf<List<Review>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        firestoreCompanyDetails.loadCompanyName(context) { name ->
            companyName.value = name ?: "No name found!"
            if (!name.isNullOrEmpty()) {
                firestoreReviews.fetchReviewsForCompany("DentWheelchair llc.") { fetchedReviews ->
                    reviews.value = fetchedReviews
                    isLoading.value = false
                }
            } else {
                isLoading.value = false
            }
        }
        firestoreCompanyDetails.loadCompanyDescription(context) { description ->
            companyDescription.value = description ?: "No description found!"
            isLoading.value = false
        }
        firestoreCompanyDetails.loadCompanyCategory(context) { category ->
            companyCategory.value = category ?: "No category found!"
            isLoading.value = false
        }
        firestoreCompanyDetails.loadCompanyWorkingHours(context) { workingHours ->
            companyWorkingHours.value = workingHours ?: 0
            isLoading.value = false
        }
        firestoreCompanyDetails.loadCompanyGeopoint(context) { geopoint ->
            companyGeoPoint.value = geopoint ?: GeoPoint(0.0, 0.0)
            isLoading.value = false
        }
    }

    if (isLoading.value) {
        Text("Loading data...", modifier = Modifier.padding(16.dp))
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(WindowInsets.navigationBars.asPaddingValues())
        ) {
            item {
                CompanyNameAndImage(companyName.value)
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.description),
                            color = isDark(onSurfaceDark, onSurfaceLight),
                            style = MaterialTheme.typography.headlineMedium,
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
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.services),
                            color = isDark(onSurfaceDark, onSurfaceLight),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )
                    },
                    supportingContent = {
                        ProvidedServicesListItem(companyCategory.value)
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.working_hours),
                            color = isDark(onSurfaceDark, onSurfaceLight),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                    },
                    supportingContent = {
                        Text("${companyWorkingHours.value}")
                    }
                )
            }
            item {
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(id = R.string.location),
                            color = isDark(onSurfaceDark, onSurfaceLight),
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                        )
                    },
                    supportingContent = {
                        CompanyLocation(geoPoint = companyGeoPoint.value, GoogleMapProvider())
                    }
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
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
                            navController.navigate("addReview/DentWheelchair llc./${UserSession.username}")

                        },
                        modifier = Modifier
                    ) {
                        Text(text = "Dodaj recenziju")
                    }
                }
            }
            item {
                ReviewList(reviews = reviews.value)
            }
        }
    }
}


@Preview
@Composable
fun CompanyDetailsPreview(){
    CompanyDetailsContent(context = LocalContext.current)
}
