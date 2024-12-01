package hr.foi.air.servicesync.backend

import com.google.firebase.firestore.FirebaseFirestore

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    fun saveReservation(
        companyId: String,
        serviceName: String,
        reservationDate: Long,
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val reservation = hashMapOf(
            "companyId" to companyId,
            "serviceName" to serviceName,
            "reservationDate" to reservationDate,
            "userId" to userId
        )

        db.collection("reservations")
            .add(reservation)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}