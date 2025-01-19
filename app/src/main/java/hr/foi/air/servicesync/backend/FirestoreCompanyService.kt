package hr.foi.air.servicesync.backend

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.text.Collator
import java.util.Locale

class FirestoreCompanyService
{

    private val db = FirebaseFirestore.getInstance()

    fun fetchCompanies(onResult: (List<Pair<String, String?>>) -> Unit)
    {
        db.collection("companies")
            .get()
            .addOnSuccessListener { documents ->
                val companies = documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: "Unknown"
                    val imageUrl = doc.getString("pictureURL")
                    if (name.isNotEmpty()) name to imageUrl else null
                }
                onResult(companies)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching companies", exception)
                onResult(emptyList())
            }
    }

    fun fetchCategories(onResult: (List<Pair<String, String>>) -> Unit)
    {
        db.collection("companies")
            .get()
            .addOnSuccessListener { documents ->
                val categories = documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: "Unknown"
                    val category = doc.getString("category") ?: "Unknown"
                    if (name.isNotEmpty() && category.isNotEmpty()) name to category else null
                }
                onResult(categories)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching categories", exception)
                onResult(emptyList())
            }
    }

    fun fetchCities(onResult: (List<Pair<String, String>>) -> Unit)
    {
        db.collection("companies")
            .get()
            .addOnSuccessListener { documents ->
                val cities = documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: "Unknown"
                    val city = doc.getString("city") ?: "Unknown"
                    if (name.isNotEmpty() && city.isNotEmpty()) name to city else null
                }
                onResult(cities)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching cities", exception)
                onResult(emptyList())
            }
    }

    fun fetchDistinctCities(onResult: (List<String>) -> Unit)
    {
        db.collection("companies")
            .get()
            .addOnSuccessListener { documents ->
                val collator = Collator.getInstance(Locale("hr"))
                val distinctCityList = documents.mapNotNull { doc ->
                    doc.getString("city")
                }.distinct().sortedWith(collator)
                onResult(distinctCityList)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching distinct cities", exception)
                onResult(emptyList())
            }
    }
}