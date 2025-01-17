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
import hr.foi.air.servicesync.backend.FirestoreService
import hr.foi.air.servicesync.business.CompanyDetailsHandler
import hr.foi.air.servicesync.business.MapProviderManager
import hr.foi.air.servicesync.business.ReservationManager
import hr.foi.air.servicesync.business.ReviewHandler
import hr.foi.air.servicesync.data.Review
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.ui.components.CompanyDescription
import hr.foi.air.servicesync.ui.components.CompanyImage
import hr.foi.air.servicesync.ui.components.CompanyLocation
import hr.foi.air.servicesync.ui.components.ReviewList
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.items.ProvidedServicesListItem


@Composable
fun CompanyDetailsContent(
    modifier: Modifier = Modifier,
    context: Context,
    navController: NavController,
    companyName: String,
) {
    val firestoreCompanyDetails = FirestoreCompanyDetails()
    val reviewHandler = ReviewHandler()
    val reservationManager = ReservationManager(FirestoreService())

    val companyDescription = remember { mutableStateOf(context.getString(R.string.loading)) }
    val companyCategory = remember { mutableStateOf(context.getString(R.string.loading)) }
    val companyOpeningTime = remember { mutableStateOf("") }
    val companyClosingTime = remember { mutableStateOf("") }
    val companyGeoPoint = remember { mutableStateOf<GeoPoint?>(null) }
    val companyImageUrl = remember { mutableStateOf<String?>(null) }
    val reviews = remember { mutableStateOf<List<Review>>(emptyList()) }
    val services = remember { mutableStateOf<List<String>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    var isButtonEnabledReservation = remember { mutableStateOf(false) }
    var isButtonEnabledReview = remember { mutableStateOf(false) }

    LaunchedEffect(companyName) {
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
        reservationManager.checkUserCompanyReservation(
            userId = UserSession.username,
            companyId = companyName,
            onSuccess = { exists ->
                isButtonEnabledReservation.value = exists
                isLoading.value = false
            },
            onFailure = { exception ->
                isButtonEnabledReservation.value = false
                isLoading.value = false
            }
        )
        reviewHandler.checkIfUserHasReview(
            userId = UserSession.username,
            onSucces = { exists ->
                isButtonEnabledReview.value = exists
                isLoading.value = false
            }
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
                            ProvidedServicesListItem(serviceName = service, onServiceClicked = {
                                navController.navigate("company/$companyName/$service")
                            })
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
                    Text("${companyOpeningTime.value} - ${companyClosingTime.value}")
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
                        Button(
                            onClick = {
                                navController.navigate("addReview/$companyName/${UserSession.username}")
                            },
                            enabled = isButtonEnabledReview.value && isButtonEnabledReservation.value,
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
