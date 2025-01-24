package hr.foi.air.servicesync.business

import hr.foi.air.servicesync.backend.FirestoreFavorites

class FavoritesHandler(private val firestoreFavorites: FirestoreFavorites = FirestoreFavorites()) {

    suspend fun addFavorite(userId: String, companyId: String) {
        firestoreFavorites.addFavorite(userId, companyId)
    }

    suspend fun removeFavorite(userId: String, companyId: String) {
        firestoreFavorites.removeFavorite(userId, companyId)
    }

    fun isFavorite(userId: String, companyId: String, callback: (Boolean) -> Unit) {
        firestoreFavorites.isFavorite(userId, companyId, callback)
    }

    fun getFavoriteCompanies(
        userId: String,
        onResult: (List<Map<String, Any>>) -> Unit
    ) {
        firestoreFavorites.fetchFavoriteCompanyIds(userId) { favoriteCompanyIds ->
            if (favoriteCompanyIds.isNotEmpty()) {
                firestoreFavorites.fetchCompaniesByIds(favoriteCompanyIds, onResult)
            } else {
                onResult(emptyList())
            }
        }
    }
}
