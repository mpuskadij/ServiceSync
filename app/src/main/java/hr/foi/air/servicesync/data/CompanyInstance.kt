package hr.foi.air.servicesync.data

import android.content.Context
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

object CompanyInstance
{
    private val firestore by lazy { FirebaseFirestore.getInstance() }

    fun fetchCompanyName(context: Context, onComplete: (String?) -> Unit)
    {
        firestore.collection("companies")
            .document("company")
            .get()
            .addOnSuccessListener { document ->
                val name = document?.getString("name")
                onComplete(name)
            }
            .addOnFailureListener {
                onComplete(null)
                Toast.makeText(context, "No data found!", Toast.LENGTH_SHORT).show()
            }
    }
    fun fetchCompanyDescription(context: Context, onComplete: (String?) -> Unit)
    {
        firestore.collection("companies")
            .document("company")
            .get()
            .addOnSuccessListener { document ->
                val description = document?.getString("description")
                onComplete(description)
            }
            .addOnFailureListener {
                onComplete(null)
                Toast.makeText(context, "No data found!", Toast.LENGTH_SHORT).show()
            }
    }
    fun fetchCompanyCategory(context: Context, onComplete: (String?) -> Unit)
    {
        firestore.collection("companies")
            .document("company")
            .get()
            .addOnSuccessListener { document ->
                val category = document?.getString("category")
                onComplete(category)
            }
            .addOnFailureListener {
                onComplete(null)
                Toast.makeText(context, "No data found!", Toast.LENGTH_SHORT).show()
            }
    }
    fun fetchCompanyWorkingHours(context: Context, onComplete: (Int?) -> Unit) {
        firestore.collection("companies")
            .document("company")
            .get()
            .addOnSuccessListener { document ->
                val workingHours = document?.getLong("workingHours")?.toInt()
                onComplete(workingHours)
            }
            .addOnFailureListener {
                onComplete(null)
                Toast.makeText(context, "No data found!", Toast.LENGTH_SHORT).show()
            }
    }

    fun fetchCompanyGeopoint(context: Context, onComplete: (GeoPoint?) -> Unit) {
        firestore.collection("companies")
            .document("company")
            .get()
            .addOnSuccessListener { document ->
                val location = document?.getGeoPoint("location") // Use getGeoPoint for GeoPoint fields
                onComplete(location)
            }
            .addOnFailureListener {
                onComplete(null)
                Toast.makeText(context, "No data found!", Toast.LENGTH_SHORT).show()
            }
    }
}