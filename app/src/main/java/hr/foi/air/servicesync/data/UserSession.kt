package hr.foi.air.servicesync.data

import com.google.firebase.auth.FirebaseAuth
import hr.foi.air.servicesync.backend.FirestoreUserDetails

object UserSession {
    val username: String
        get() = FirebaseAuth.getInstance().currentUser?.email
            ?: throw IllegalStateException("No user is currently logged in")
    fun isUserLoggedIn(): Boolean {
        return FirebaseAuth.getInstance().currentUser != null
    }

    fun logout() {
        val firesoreUserDetails : FirestoreUserDetails = FirestoreUserDetails()
        firesoreUserDetails.deleteFCMToken{ success ->
            FirebaseAuth.getInstance().signOut()
        }
    }
}