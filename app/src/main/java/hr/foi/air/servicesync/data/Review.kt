package hr.foi.air.servicesync.data

data class Review(
    val companyId: String = "",
    val description: String = "",
    val rating: Int = 0,
    val userId: String = ""
)
