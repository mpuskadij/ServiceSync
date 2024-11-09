package hr.foi.air.servicesync.backend

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreUserDetails {
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid

    var name: String = ""
    var surname: String = ""
    var username: String = ""
    var description: String = ""

    fun loadUserDetails(onResult: (Boolean) -> Unit) {
        userId?.let { id ->
            firestore.collection("users").document(id).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        name = document.getString("name") ?: ""
                        surname = document.getString("surname") ?: ""
                        username = document.getString("username") ?: ""
                        description = document.getString("description") ?: ""
                        onResult(true)
                    } else {
                        onResult(false)
                    }
                }
                .addOnFailureListener{
                    onResult(false)
                }
        } ?: onResult(false)
    }
    fun saveUserDetails(
        name: String,
        surname: String,
        username: String,
        description: String,
        onResult: (Boolean) -> Unit
    ) {
        userId?.let { id ->
            val userDetails = mapOf(
                "name" to name,
                "surname" to surname,
                "username" to username,
                "description" to description
            )
            firestore.collection("users").document(id).update(userDetails)
                .addOnSuccessListener {
                    onResult(true)
                }
                .addOnFailureListener { exception ->
                    firestore.collection("users").document(id).set(userDetails)
                        .addOnSuccessListener {
                            onResult(true)
                        }
                        .addOnFailureListener {
                            onResult(false)
                        }
                }
        } ?: onResult(false)
    }
    fun updatePassword(newPassword: String, onResult: (Boolean) -> Unit) {
        val user = auth.currentUser
        user?.updatePassword(newPassword)
            ?.addOnSuccessListener {
                onResult(true)
            }
            ?.addOnFailureListener {
                onResult(false)
            }
    }
}