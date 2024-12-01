package hr.foi.air.servicesync.backend

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.FirebaseFirestore
import hr.foi.air.servicesync.data.CompanyInstance

class FirestoreCompanyDetails {

    private val db = FirebaseFirestore.getInstance()

    private var companyName: String = "No name found!"
    private var companyDescription: String = "No description found!"
    private var companyCategory: String = "No category found!"
    private var companyWorkingHours: Int = 0
    private var companyGeoPoint: GeoPoint = GeoPoint(0.0, 0.0)

    fun loadCompanyName(context: Context, onResult: (String?) -> Unit) {
        CompanyInstance.fetchCompanyName(context) { name ->
            companyName = name ?: "No name found!"
            onResult(companyName)
        }
    }

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

}
