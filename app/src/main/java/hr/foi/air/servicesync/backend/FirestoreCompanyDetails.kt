package hr.foi.air.servicesync.backend

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import hr.foi.air.servicesync.data.CompanyInstance

class FirestoreCompanyDetails {

    private val db = FirebaseFirestore.getInstance()

    private var companyName: String = "No name found!"
    private var companyDescription: String = "No description found!"
    private var companyCategory: String = "No category found!"
    private var companyWorkingHours: Int = 0
    private var companyOpeningTime: String = "0:00"
    private var companyClosingTime: String = "0:00"
    private var companyGeoPoint: GeoPoint = GeoPoint(0.0, 0.0)

    fun loadCompanyName(context: Context, onResult: (String?) -> Unit) {
        CompanyInstance.fetchCompanyName(context) { name ->
            companyName = name ?: "No name found!"
            onResult(companyName)
        }
    }

    fun loadCompanyDescription(context: Context, onResult: (String?) -> Unit) {
        CompanyInstance.fetchCompanyDescription(context) { description ->
            companyDescription = description ?: "No description found!"
            onResult(companyDescription)
        }
    }

    fun loadCompanyCategory(context: Context, onResult: (String?) -> Unit) {
        CompanyInstance.fetchCompanyCategory(context) { category ->
            companyCategory = category ?: "No category found!"
            onResult(companyCategory)
        }
    }

    fun loadCompanyWorkingHours(context: Context, onResult: (Int?) -> Unit) {
        CompanyInstance.fetchCompanyWorkingHours(context) { workingHours ->
            companyWorkingHours = workingHours ?: 0
            onResult(companyWorkingHours)
        }
    }

    fun loadCompanyGeopoint(context: Context, onResult: (GeoPoint?) -> Unit) {
        CompanyInstance.fetchCompanyGeopoint(context) { geopoint ->
            companyGeoPoint = geopoint ?: GeoPoint(0.0, 0.0)
            onResult(companyGeoPoint)
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
}
