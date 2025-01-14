package hr.foi.air.servicesync.business

import android.content.Context
import androidx.compose.runtime.MutableState
import com.google.firebase.firestore.GeoPoint
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.backend.FirestoreCompanyDetails
import hr.foi.air.servicesync.data.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompanyDetailsHandler {

    suspend fun getCompanyDetails(
        context: Context,
        companyName: String,
        companyDescription: MutableState<String>,
        companyCategory: MutableState<String>,
        companyWorkingHours: MutableState<Int>,
        companyGeoPoint: MutableState<GeoPoint?>,
        companyImageUrl: MutableState<String?>,
        reviews: MutableState<List<Review>>,
        services: MutableState<List<String>>,
        isLoading: MutableState<Boolean>,
        firestoreCompanyDetails: FirestoreCompanyDetails,
        reviewHandler: ReviewHandler
    ) {
        withContext(Dispatchers.IO) {
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
                companyGeoPoint.value = geoPoint
            }

            firestoreCompanyDetails.loadCompanyImageUrlByName(companyName) { imageUrl ->
                companyImageUrl.value = imageUrl
            }

            reviewHandler.fetchReviews(companyName) { fetchedReviews ->
                reviews.value = fetchedReviews
            }

            firestoreCompanyDetails.loadCompanyServicesByName(companyName) { fetchedServices ->
                services.value = fetchedServices ?: emptyList()
            }

            isLoading.value = false
        }
    }
}
