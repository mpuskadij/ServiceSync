package hr.foi.air.servicesync.data

import com.google.firebase.auth.FirebaseAuth

object UserSession {
    val username: String
        get() = FirebaseAuth.getInstance().currentUser?.email
            ?: throw IllegalStateException("No user is currently logged in")
}