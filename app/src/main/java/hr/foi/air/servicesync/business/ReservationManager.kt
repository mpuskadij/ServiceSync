package hr.foi.air.servicesync.business

import hr.foi.air.servicesync.backend.FirestoreService

class ReservationManager(private val firestoreService: FirestoreService) {

    fun saveReservation(
        companyId: String,
        serviceName: String,
        reservationDate: Long,
        userId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (reservationDate <= System.currentTimeMillis()) {
            onFailure(IllegalArgumentException("Rezervacija mora biti u budućnosti!.")) //Već postoji, vjerojatno nepotrebno, za svaki slučaj....
            return
        }

        firestoreService.saveReservation(companyId, serviceName, reservationDate, userId, onSuccess, onFailure)
    }

    fun getAvailableTimeSlots(
        companyId: String,
        date: Long,
        onSuccess: (List<Long>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreService.getAvailableTimeSlots(companyId, date, onSuccess, onFailure)
    }
}