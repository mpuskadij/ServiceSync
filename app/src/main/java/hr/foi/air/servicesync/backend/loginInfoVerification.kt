package hr.foi.air.servicesync.backend

class loginInfoVerification {
    fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()
        return email.matches(emailRegex)
    }
    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }
}