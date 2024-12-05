package hr.foi.air.servicesync.business

import hr.foi.air.servicesync.backend.FirestoreReviews
import hr.foi.air.servicesync.data.Review

class ReviewHandler(
    private val firestoreReviews: FirestoreReviews = FirestoreReviews()
) {
    fun fetchReviews(companyName: String, onResult: (List<Review>) -> Unit) {
        firestoreReviews.fetchReviewsForCompany(companyName, onResult)
    }

    fun addReview(review: Review, onResult: (Boolean) -> Unit) {
        firestoreReviews.addReview(review) { success ->
            onResult(success)
        }
    }
}
