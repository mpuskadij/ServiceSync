package hr.foi.air.servicesync.ui.contents

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import hr.foi.air.servicesync.business.ReviewHandler
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.ui.components.CompanyCard


val userId = UserSession.username


fun getFavoriteCompanies(userId: String, onResult: (List<String>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("userFavorites")
        .whereEqualTo("userId", userId)
        .get()
        .addOnSuccessListener { result ->
            val companyIds = result.documents.mapNotNull { it.getString("companyId") }
            onResult(companyIds)
        }
        .addOnFailureListener { exception ->
            Log.e("Favorites", "Error fetching favorites: ${exception.message}")
            onResult(emptyList())
        }
}

@Composable
fun FavoriteContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val companies = remember { mutableStateOf(emptyList<Map<String, Any>>()) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        isLoading.value = true

        // Dohvati popis omiljenih companyId (ne cijele tvrtke)
        getFavoriteCompanies(UserSession.username) { favoriteCompanyIds ->
            if (favoriteCompanyIds.isNotEmpty()) {
                val db = FirebaseFirestore.getInstance()
                db.collection("companies")
                    .whereIn(FieldPath.documentId(), favoriteCompanyIds) // Filtriraj po ID-jevima
                    .get()
                    .addOnSuccessListener { documents ->
                        companies.value = documents.map { it.data }
                        isLoading.value = false
                    }
                    .addOnFailureListener {
                        companies.value = emptyList()
                        isLoading.value = false
                    }
            } else {
                companies.value = emptyList()
                isLoading.value = false
            }
        }
    }

    if (isLoading.value) {
        // Prikaži loader dok se učitavaju podaci
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        // Prikaži omiljene firme
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