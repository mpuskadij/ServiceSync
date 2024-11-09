package hr.foi.air.servicesync.business

import android.content.Context
import hr.foi.air.servicesync.backend.FirestoreUserDetails

class UserDataHandler {

    private val firestoreUserDetails = FirestoreUserDetails()

    fun loadUserDetails(onResult: (Map<String, String>?) -> Unit) {
        firestoreUserDetails.loadUserDetails { success ->
            if (success) {
                val userDetails = mapOf(
                    "name" to firestoreUserDetails.name,
                    "surname" to firestoreUserDetails.surname,
                    "username" to firestoreUserDetails.username,
                    "email" to firestoreUserDetails.email,
                    "description" to firestoreUserDetails.description
                )
                onResult(userDetails)
            } else {
                onResult(null)
            }
        }
    }

    fun saveUserDetails(
        name: String,
        surname: String,
        username: String,
        description: String,
        onResult: (Boolean) -> Unit
    ) {
        firestoreUserDetails.saveUserDetails(name, surname, username, description) { success ->
            onResult(success)
        }
    }

    fun updatePassword(newPassword: String, onResult: (Boolean) -> Unit) {
        firestoreUserDetails.updatePassword(newPassword) { success ->
            onResult(success)
        }
    }
}
