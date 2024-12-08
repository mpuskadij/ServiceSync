package hr.foi.air.servicesync.backend

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class FirestoreCompanyDetails {

    private val db = FirebaseFirestore.getInstance()

    private var companyName: String = "No name found!"
    private var companyDescription: String = "No description found!"
    private var companyCategory: String = "No category found!"
    private var companyWorkingHours: Int = 0
    private var companyOpeningTime: String = "0:00"
    private var companyClosingTime: String = "0:00"
    private var companyGeoPoint: GeoPoint = GeoPoint(0.0, 0.0)

    fun loadCompanyDescriptionByName(companyName: String, onResult: (String?) -> Unit) {
        db.collection("companies")
            .whereEqualTo("name", companyName)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                val description = documents.documents.firstOrNull()?.getString("description")
                companyDescription = description ?: "No description found!"
                onResult(companyDescription)
            }
            .addOnFailureListener {
                onResult("No description found!")
            }
    }

    fun loadCompanyCategoryByName(companyName: String, onResult: (String?) -> Unit) {
        db.collection("companies")
            .whereEqualTo("name", companyName)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                val category = documents.documents.firstOrNull()?.getString("category")
                companyCategory = category ?: "No category found!"
                onResult(companyCategory)
            }
            .addOnFailureListener {
                onResult("No category found!")
            }
    }

    fun loadCompanyWorkingHoursByName(companyName: String, onResult: (Int?) -> Unit) {
        db.collection("companies")
            .whereEqualTo("name", companyName)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                val workingHours = documents.documents.firstOrNull()?.getLong("workingHours")?.toInt()
                companyWorkingHours = workingHours ?: 0
                onResult(companyWorkingHours)
            }
            .addOnFailureListener {
                onResult(0)
            }
    }

    fun loadCompanyImageUrlByName(companyName: String, onResult: (String?) -> Unit) {
        db.collection("companies")
            .whereEqualTo("name", companyName)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                val imageUrl = documents.documents.firstOrNull()?.getString("pictureURL")
                onResult(imageUrl)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching image URL for company: $companyName", exception)
                onResult(null)
            }
    }

    fun loadCompanyGeopointByName(companyName: String, onResult: (GeoPoint?) -> Unit) {
        db.collection("companies")
            .whereEqualTo("name", companyName)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.e("Firestore", "No documents found for company: $companyName")
                    onResult(GeoPoint(0.0, 0.0))
                    return@addOnSuccessListener
                }

                val geopoint = documents.documents.firstOrNull()?.getGeoPoint("location")
                if (geopoint != null) {
                    Log.d("Firestore", "Found GeoPoint: $geopoint for company: $companyName")
                    companyGeoPoint = geopoint
                    onResult(companyGeoPoint)
                } else {
                    Log.e("Firestore", "GeoPoint not found for company: $companyName")
                    onResult(GeoPoint(0.0, 0.0))
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching GeoPoint for company: $companyName", exception)
                onResult(GeoPoint(0.0, 0.0))
            }
    }

    fun loadCompanyOpeningTimeByName(companyName: String, onResult: (String?) -> Unit) {
        db.collection("companies")
            .whereEqualTo("name", companyName)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                val openingTime = documents.documents.firstOrNull()?.getString("openingTime")
                companyOpeningTime = openingTime ?: "0:00"
                onResult(companyOpeningTime)
            }
            .addOnFailureListener {
                onResult("0:00")
            }
    }
    fun loadCompanyClosingTimeByName(companyName: String, onResult: (String?) -> Unit) {
        db.collection("companies")
            .whereEqualTo("name", companyName)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                val closingTime = documents.documents.firstOrNull()?.getString("closingTime")
                companyClosingTime = closingTime ?: "23:59"
                onResult(companyClosingTime)
            }
            .addOnFailureListener {
                onResult("23:59")
            }
    }
    fun loadCompanyOpeningTimeById(companyId: String, onResult: (String?) -> Unit) {
        db.collection("companies")
            .document(companyId)
            .get()
            .addOnSuccessListener { document ->
                val closingTime = document.getString("openingTime")
                val formattedOpeningTime = closingTime ?: "23:59"
                onResult(formattedOpeningTime)
            }
            .addOnFailureListener {
                onResult("23:59")
            }
    }
    fun loadCompanyClosingTimeById(companyId: String, onResult: (String?) -> Unit) {
        db.collection("companies")
            .document(companyId)
            .get()
            .addOnSuccessListener { document ->
                val closingTime = document.getString("closingTime")
                val formattedClosingTime = closingTime ?: "23:59"
                onResult(formattedClosingTime)
            }
            .addOnFailureListener {
                onResult("23:59")
            }
    }
}
