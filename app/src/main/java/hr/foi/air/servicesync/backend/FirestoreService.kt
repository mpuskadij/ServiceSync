package hr.foi.air.servicesync.backend

import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalTime

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    private fun timeToMillis(timeString: String, startOfDay: Long): Long {
        val formattedTime = if (timeString.length == 4) "0$timeString" else timeString
        val time = LocalTime.parse(formattedTime)
        val secondsSinceMidnight = time.toSecondOfDay()
        return secondsSinceMidnight * 1000L + startOfDay
    }


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
            "userId" to userId,
            "notificationSent" to false
        )
        val documentId = "$companyId-$userId-$reservationDate"

        db.collection("reservations")
            .document(documentId).set(reservation)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun getAvailableTimeSlotsAndRange(
        companyId: String,
        serviceName: String,
        date: Long,
        onSuccess: (List<Long>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val startOfDay = date - (date % (24 * 60 * 60 * 1000))
        val endOfDay = startOfDay + (24 * 60 * 60 * 1000) - 1

        val firestoreCompanyDetails = FirestoreCompanyDetails()
        firestoreCompanyDetails.loadCompanyOpeningTimeById(companyId) { openingTime ->
            firestoreCompanyDetails.loadCompanyClosingTimeById(companyId) { closingTime ->
                firestoreCompanyDetails.loadServiceDuration(
                    companyId,
                    serviceName
                ) { duration ->
                    val openingMillis = timeToMillis(openingTime ?: "00:00", startOfDay)
                    val closingMillis = timeToMillis(closingTime ?: "23:59", startOfDay)

                    val step = (duration?.toLong() ?: 30L) * 60 * 1000 // default time slot of 30 minutes
                    val allSlots = (openingMillis - (60 * 60 * 1000) until closingMillis - (60 * 60 * 1000) step step).toList()

                    db.collection("reservations")
                        .whereEqualTo("companyId", companyId)
                        .whereGreaterThanOrEqualTo("reservationDate", startOfDay)
                        .whereLessThanOrEqualTo("reservationDate", endOfDay)
                        .get()
                        .addOnSuccessListener { documents ->
                            val occupiedSlots = documents.map { it["reservationDate"] as Long }
                            val availableSlots = allSlots.filter { slotStart ->
                                occupiedSlots.none { occupied ->
                                    val slotEnd = slotStart + step
                                    occupied in slotStart until slotEnd
                                }
                            }

                            onSuccess(availableSlots)
                        }
                        .addOnFailureListener { exception ->
                            onFailure(exception)
                        }
                }
            }
        }
    }

    fun fetchUserReservations(
        userId: String,
        onSuccess: (List<Map<String, Any>>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("reservations")
            .whereEqualTo("userId", userId)
            .whereGreaterThan("reservationDate", System.currentTimeMillis())
            .get()
            .addOnSuccessListener { documents ->
                val reservations = documents.map { it.data }
                onSuccess(reservations)
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun checkUserCompanyReservation(
        userId: String,
        companyId: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("reservations")
            .whereEqualTo("userId", userId)
            .whereEqualTo("companyId", companyId)
            .get()
            .addOnSuccessListener { documents ->
                val exists = !documents.isEmpty
                onSuccess(exists)
            }
            .addOnFailureListener { onFailure(it) }
    }


}