package hr.foi.air.servicesync.backend

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.util.Log

class firebaseAuthentication {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    public var currentUser : FirebaseUser? = null
    fun registerUser(email: String, password: String, callback: (Boolean, FirebaseUser?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("FirebaseAuth", "User registration successful: ${user?.email}")
                    callback(true, user)
                } else {
                    Log.e("FirebaseAuth", "User registration failed: ${task.exception?.message}")
                    callback(false, null)
                }
            }
    }
    fun loginUser(email: String, password: String, callback: (Boolean, FirebaseUser?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    currentUser = auth.currentUser
                    Log.d("FirebaseAuth", "User successfuly logged in: ${currentUser?.email}")
                    callback(true, currentUser)
                } else {
                    Log.e("FirebaseAuth", "Login failed: ${task.exception?.message}")
                    callback(false, null)
                }
            }
    }
}