package hr.foi.air.servicesync.backend

import android.content.Context
import hr.foi.air.servicesync.data.CompanyInstance

class FirestoreCompanyDetails
{
    private var companyName: MutableMap<String, String> = mutableMapOf()
    private var companyDescription: MutableMap<String, String> = mutableMapOf()
    private var companyCategory: MutableMap<String, String> = mutableMapOf()
    private var companyWorkingHours: MutableMap<String, String> = mutableMapOf()
    private var companyGeoPoint: MutableMap<String, String> = mutableMapOf()

    fun loadCompanyName(context: Context, onResult: (String?) -> Unit)
    {
        CompanyInstance.fetchCompanyName(context) { name ->
            companyName["name"] = name ?: "No name found!"
            onResult(name)
        }
    }

    fun loadCompanyDescription(context: Context, onResult: (String?) -> Unit)
    {
        CompanyInstance.fetchCompanyDescription(context) { description ->
            companyDescription["description"] = description ?: "No description found!"
            onResult(description)
        }
    }

    fun loadCompanyCategory(context: Context, onResult: (String?) -> Unit)
    {
        CompanyInstance.fetchCompanyCategory(context) { category ->
            companyCategory["category"] = category ?: "No category found!"
            onResult(category)
        }
    }

    fun loadCompanyWorkingHours(context: Context, onResult: (String?) -> Unit)
    {
        CompanyInstance.fetchCompanyWorkingHours(context) { workingHours ->
            companyWorkingHours["workingHours"] = workingHours ?: "No working hours found!"
            onResult(workingHours)
        }
    }

    fun loadCompanyGeopoint(context: Context, onResult: (String?) -> Unit)
    {
        CompanyInstance.fetchCompanyGeopoint(context) { geopoint ->
            companyWorkingHours["workingHours"] = geopoint ?: "No working hours found!"
            onResult(geopoint)
        }
    }
}