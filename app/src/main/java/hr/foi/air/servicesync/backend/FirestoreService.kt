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
        val documentId = "$companyId-$userId-$reservationDate"

        db.collection("reservations")
            .document(documentId).set(reservation)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getAvailableTimeSlots(
        companyId: String,
        date: Long,
        onSuccess: (List<Long>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val startOfDay = date - (date % (24 * 60 * 60 * 1000)) // Početak dana
        val endOfDay = startOfDay + (24 * 60 * 60 * 1000) - 1 // Kraj dana

        db.collection("reservations")
            .whereEqualTo("companyId", companyId)
            .whereGreaterThanOrEqualTo("reservationDate", startOfDay)
            .whereLessThanOrEqualTo("reservationDate", endOfDay)
            .get()
            .addOnSuccessListener { documents ->
                val occupiedSlots = documents.map { it["reservationDate"] as Long }
                val allSlots = (-1..22).flatMap { hour ->
                    listOf(
                        startOfDay + hour * 60 * 60 * 1000, // Početak svakog sata
                        startOfDay + hour * 60 * 60 * 1000 + 30 * 60 * 1000 // Polusatni interval
                    )
                }.filter { it <= endOfDay }
                val availableSlots = allSlots.filter { it !in occupiedSlots }
                onSuccess(availableSlots)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}