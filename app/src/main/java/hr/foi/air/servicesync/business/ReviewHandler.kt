package hr.foi.air.servicesync.business

import hr.foi.air.servicesync.backend.FirestoreReviews
import hr.foi.air.servicesync.data.Review
import java.math.BigDecimal
import java.math.RoundingMode

class ReviewHandler(
    private val firestoreReviews: FirestoreReviews = FirestoreReviews()
) {
    fun fetchReviews(companyName: String, onResult: (List<Review>) -> Unit) {
        firestoreReviews.fetchReviewsForCompany(companyName) { reviews ->
            onResult(reviews)
        }
    }

    fun fetchAndCalculateReviewAverage(
        companyName: String,
        onResult: (Double) -> Unit
    ) {
        firestoreReviews.fetchReviewsForCompany(companyName) { reviews ->
            val average = if (reviews.isEmpty()) 0.0 else reviews.map { it.rating }.average()
            val roundedAverage = BigDecimal(average).setScale(1, RoundingMode.HALF_UP).toDouble()
            onResult(roundedAverage)
        }
    }

    fun addReview(review: Review, onResult: (Boolean) -> Unit) {
        firestoreReviews.addReview(review) { success ->
            onResult(success)
        }
    }
}
