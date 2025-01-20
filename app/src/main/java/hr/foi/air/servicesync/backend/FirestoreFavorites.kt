package hr.foi.air.servicesync.backend

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirestoreFavorites {

    private val firestore = FirebaseFirestore.getInstance()
    private val favoritesCollection = firestore.collection("userFavorites")

    suspend fun addFavorite(userId: String, companyId: String) {
        val favoriteData = mapOf(
            "userId" to userId,
            "companyId" to companyId
        )
        favoritesCollection.add(favoriteData).await()
    }

    suspend fun removeFavorite(userId: String, companyId: String) {
        val querySnapshot = favoritesCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("companyId", companyId)
            .get()
            .await()

        for (document in querySnapshot.documents) {
            favoritesCollection.document(document.id).delete().await()
        }
    }

    fun isFavorite(userId: String, companyId: String, callback: (Boolean) -> Unit) {
        favoritesCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("companyId", companyId)
            .get()
            .addOnSuccessListener { documents ->
                callback(documents.size() > 0)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun fetchFavoriteCompanyIds(userId: String, onResult: (List<String>) -> Unit) {
        firestore.collection("userFavorites")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val companyIds = result.documents.mapNotNull { it.getString("companyId") }
                onResult(companyIds)
            }
            .addOnFailureListener { exception ->
                Log.e("FavoriteDataSource", "Error fetching favorite IDs: ${exception.message}")
                onResult(emptyList())
            }
    }
    fun fetchCompaniesByIds(companyIds: List<String>, onResult: (List<Map<String, Any>>) -> Unit) {
        firestore.collection("companies")
            .whereIn(FieldPath.documentId(), companyIds)
            .get()
            .addOnSuccessListener { documents ->
                val companies = documents.map { it.data }
                onResult(companies)
            }
            .addOnFailureListener { exception ->
                Log.e("CompanyDataSource", "Error fetching companies: ${exception.message}")
                onResult(emptyList())
            }
    }
}
