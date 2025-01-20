package hr.foi.air.servicesync.backend

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import hr.foi.air.servicesync.data.UserSession

class FirestoreUserDetails {
    private var firestore = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    private val userId = auth.currentUser?.uid

    var name: String = ""
    var surname: String = ""
    var username: String = ""
    var email: String = ""
    var description: String = ""

    fun registerUserDetails(
        email: String,
        password: String,
        onResult: (Boolean) -> Unit
    ) {
        val userDetails = mapOf(
            "email" to email,
            "username" to email.substringBefore("@"),
            "name" to "",
            "surname" to "",
            "description" to "New user",
            "password" to password
        )
        firestore.collection("users").document(email).set(userDetails)
            .addOnSuccessListener {
                Log.d("loginRegisterHandler", "User details added to Firestore")
                onResult(true)
            }
            .addOnFailureListener { exception ->
                Log.e("loginRegisterHandler", "Failed to add user details to Firestore", exception)
                onResult(false)
            }
    }

    fun loadUserDetails(onResult: (Boolean) -> Unit) {
        UserSession.username.let { id ->
            firestore.collection("users").document(UserSession.username).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        name = document.getString("name") ?: ""
                        surname = document.getString("surname") ?: ""
                        username = document.getString("username") ?: ""
                        email = document.getString("email") ?: ""
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
        UserSession.username.let { id ->
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
    //FOR MESSAGING
    fun saveFCMToken(token: String,onResult: (Boolean) -> Unit) {
        UserSession.username.let { id ->
            val userDetails = mapOf(
                "FCMToken" to token
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
        }
    }
    fun updateFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    saveFCMToken(token){ success ->

                    }
                    Log.d("FCM", "FCM Token: $token")
                } else {
                    Log.e("FCM", "Failed to retrieve FCM token", task.exception)
                }
            }
    }
    fun deleteFCMToken(onResult: (Boolean) -> Unit) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    saveFCMToken("") { success ->
                        Log.d("FCM", "FCM Token: $token")
                        if(success){
                            onResult(true)
                        }
                        onResult(false)
                    }
                } else {
                    Log.e("FCM", "Failed to retrieve FCM token", task.exception)
                    onResult(false)
                }
            }
    }
    fun saveProfileImageUrlToFirestore(userId: String, imageUrl: String) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("profilePics").document(userId)

        userRef.set(mapOf("photoURL" to imageUrl))
            .addOnSuccessListener {
                println("Profile picture URL updated successfully.")
            }
            .addOnFailureListener { exception ->
                println("Error updating profile picture URL: ${exception.message}")
            }
    }

    fun getProfileImageUrl(userId: String, onImageUrlFetched: (String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val userRef = db.collection("profilePics").document(userId)

        userRef.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val imageUrl = documentSnapshot.getString("photoURL")
                    onImageUrlFetched(imageUrl)
                } else {
                    onImageUrlFetched(null)
                }
            }
            .addOnFailureListener { exception ->
                println("Error fetching profile picture URL: ${exception.message}")
                onImageUrlFetched(null)
            }
    }
}