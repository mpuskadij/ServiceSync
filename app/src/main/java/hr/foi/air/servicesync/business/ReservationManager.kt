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
            onFailure(IllegalArgumentException("Rezervacija mora biti u budućnosti!.")) //Već postoji provjera, ali služi pa molim ne brisati
            return
        }

        firestoreService.saveReservation(companyId, serviceName, reservationDate, userId, onSuccess, onFailure)
    }

    fun fetchAvailableSlots(
        companyId: String,
        serviceName: String,
        dateMillis: Long,
        onSlotsFetched: (List<Long>) -> Unit
    ) {
        firestoreService.getAvailableTimeSlotsAndRange(
            companyId,
            serviceName,
            dateMillis,
            onSuccess = { slots -> onSlotsFetched(slots) },
            onFailure = { exception ->
                println("Error fetching available slots: ${exception.message}")
            }
        )
    }

    fun fetchUserReservations(
        userId: String,
        onReservationsFetched: (List<Map<String, Any>>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestoreService.fetchUserReservations(userId, onReservationsFetched, onFailure)
    }

    fun checkUserCompanyReservation(
        userId: String,
        companyId: String,
        onSuccess: (Boolean) -> Unit,
        onFailure: (Exception) -> Unit
    ){
        firestoreService.checkUserCompanyReservation(userId, companyId, onSuccess, onFailure)
    }
}