package hr.foi.air.servicesync.business

import android.content.Context
import androidx.compose.runtime.MutableState
import com.google.firebase.firestore.GeoPoint
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.backend.FirestoreCompanyDetails
import hr.foi.air.servicesync.backend.FirestoreCompanyService
import hr.foi.air.servicesync.data.Review
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CompanyDetailsHandler {

    suspend fun getCompanyDetails(
        context: Context,
        companyName: String,
        companyDescription: MutableState<String>,
        companyCategory: MutableState<String>,
        companyOpeningTime: MutableState<String>,
        companyClosingTime: MutableState<String>,
        companyGeoPoint: MutableState<GeoPoint?>,
        companyImageUrl: MutableState<String?>,
        reviews: MutableState<List<Review>>,
        services: MutableState<List<String>>,
        isLoading: MutableState<Boolean>,
        firestoreCompanyDetails: FirestoreCompanyDetails,
        reviewHandler: ReviewHandler,
    ) {
        withContext(Dispatchers.IO) {
            firestoreCompanyDetails.loadCompanyDescriptionByName(companyName) { description ->
                companyDescription.value = description ?: context.getString(R.string.no_description)
            }

            firestoreCompanyDetails.loadCompanyCategoryByName(companyName) { category ->
                companyCategory.value = category ?: context.getString(R.string.no_category)
            }

            firestoreCompanyDetails.loadCompanyOpeningTimeByName(companyName) { openingTime ->
                companyOpeningTime.value = openingTime ?: "0:00"
            }

            firestoreCompanyDetails.loadCompanyClosingTimeByName(companyName) { closingTime ->
                companyClosingTime.value = closingTime ?: "23:59"
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

    fun getCompanyData(
        onSuccess: (
            companies: List<Pair<String, String?>>,
            categories: List<Pair<String, String>>,
            cities: List<Pair<String, String>>,
            distinctCities: List<String>) -> Unit,
        onFailure: () -> Unit
    ) {
        val firestoreService = FirestoreCompanyService()
        val companies = mutableListOf<Pair<String, String?>>()
        val categories = mutableListOf<Pair<String, String>>()
        val cities = mutableListOf<Pair<String, String>>()
        val distinctCities = mutableListOf<String>()

        firestoreService.fetchCompanies { companyList ->
            companies.addAll(companyList)
            firestoreService.fetchCategories { categoryList ->
                categories.addAll(categoryList)
                firestoreService.fetchCities { cityList ->
                    cities.addAll(cityList)
                    firestoreService.fetchDistinctCities { distinctCityList ->
                        distinctCities.addAll(distinctCityList)
                        onSuccess(companies, categories, cities, distinctCities)
                    }
                }
            }
        }

    }

}