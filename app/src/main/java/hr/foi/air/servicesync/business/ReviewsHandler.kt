package hr.foi.air.servicesync.business

import hr.foi.air.servicesync.backend.FirestoreReviews
import hr.foi.air.servicesync.data.Review

class ReviewsHandler(private val firestoreService: FirestoreReviews) {

    suspend fun getReviews(): List<Review> {
        return firestoreService.fetchReviews()
    }
}
