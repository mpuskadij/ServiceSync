package hr.foi.air.servicesync.backend

import android.content.Context
import hr.foi.air.servicesync.data.CompanyInstance

class FirestoreCompanyDetails
{
    var companyName: MutableMap<String, String> = mutableMapOf()
    var companyDescription: MutableMap<String, String> = mutableMapOf()
    var companyCategory: MutableMap<String, String> = mutableMapOf()

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
}