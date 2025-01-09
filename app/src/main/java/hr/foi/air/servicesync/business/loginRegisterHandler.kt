package hr.foi.air.servicesync.business

import android.util.Log
import hr.foi.air.servicesync.backend.firebaseAuthentication
import hr.foi.air.servicesync.backend.loginInfoVerification

class loginRegisterHandler {
    var authenticator :firebaseAuthentication = firebaseAuthentication()
    var userDataHandler = UserDataHandler()

    fun loginUser(email: String, password: String, callback: (Boolean) -> Unit) {
        val verification = loginInfoVerification()
        if(!verification.isEmailValid(email)||!verification.isPasswordValid(password)){
            callback(false)
        }
        authenticator.loginUser(email, password) { success, user ->
            if (user != null) {
                Log.d("loginRegisterHandler", "Login successful! User: $user")
                callback(true)
            } else {
                Log.e("loginRegisterHandler", "Login failed: ${success}")
                callback(false)
            }
        }
    }

    fun registerUser(email: String, password: String, callback: (Boolean) -> Unit) {
        authenticator.registerUser(email, password) { success, user ->
            if (user != null) {
                Log.d("loginRegisterHandler", "Registration successful! User: $user")
                userDataHandler.registerUserDetails(email, password) {}
                callback(true)
            } else {
                Log.e("loginRegisterHandler", "Registration failed: ${success}")
                callback(false)
            }
        }
    }
}