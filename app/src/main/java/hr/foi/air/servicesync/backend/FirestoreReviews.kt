package hr.foi.air.servicesync.backend

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import hr.foi.air.servicesync.data.Review

class FirestoreReviews {

    private val db = FirebaseFirestore.getInstance()

    fun fetchAllReviews(onResult: (List<Review>) -> Unit) {
        db.collection("reviews")
            .get()
            .addOnSuccessListener { documents ->
                val reviewsList = documents.documents.mapNotNull { document ->
                    document.toObject(Review::class.java)
                }
                onResult(reviewsList)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreReviews", "Error fetching all reviews", exception)
                onResult(emptyList())
            }
    }

    fun fetchReviewsForCompany(companyId: String, onResult: (List<Review>) -> Unit) {
        db.collection("reviews")
            .whereEqualTo("companyId", companyId.trim())
            .get()
            .addOnSuccessListener { documents ->
                val reviewsList = documents.documents.mapNotNull { document ->
                    document.toObject(Review::class.java)
                }
                Log.d("FirestoreReviews", "Fetched reviews: $reviewsList")
                onResult(reviewsList)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreReviews", "Error fetching reviews for company: $companyId", exception)
                onResult(emptyList())
            }
    }
    fun addReview(review: Review, onResult: (Boolean) -> Unit) {
        db.collection("reviews")
            .add(review)
            .addOnSuccessListener {
                Log.d("FirestoreReviews", "Review added successfully for company: ${review.companyId}")
                onResult(true)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreReviews", "Error adding review for company: ${review.companyId}", exception)
                onResult(false)
            }
    }
}