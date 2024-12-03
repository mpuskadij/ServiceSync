package hr.foi.air.servicesync.backend

import com.google.firebase.firestore.FirebaseFirestore
import hr.foi.air.servicesync.data.Review
import kotlinx.coroutines.tasks.await

class FirestoreReviews {
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun fetchReviews(): List<Review> {
        return try {
            val reviewsList = firestore.collection("reviews")
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    document.toObject(Review::class.java)
                }
            reviewsList
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

}