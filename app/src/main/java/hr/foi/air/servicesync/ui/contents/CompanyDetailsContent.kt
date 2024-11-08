package hr.foi.air.servicesync.ui.contents

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.backend.FirestoreCompanyDetails

@Composable
fun CompanyDetailsContent(
    modifier: Modifier = Modifier,
    firestoreCompanyDetails: FirestoreCompanyDetails,
    context: Context
)
{
    val companyName = remember { mutableStateOf("Loading...") }
    val companyDescription = remember { mutableStateOf("Loading...") }
    val companyCategory = remember { mutableStateOf("Loading...") }
    val companyWorkingHours = remember { mutableStateOf("Loading...") }
    val companyGeoPoint = remember { mutableStateOf("Loading...") }

    LaunchedEffect(Unit)
    {
        firestoreCompanyDetails.loadCompanyName(context) { name ->
            companyName.value = name ?: "No name found!"
        }
        firestoreCompanyDetails.loadCompanyDescription(context) { description ->
            companyDescription.value = description ?: "No description found!"
        }
        firestoreCompanyDetails.loadCompanyCategory(context) { category ->
            companyCategory.value = category ?: "No category found!"
        }
        firestoreCompanyDetails.loadCompanyWorkingHours(context) { workingHours ->
            companyWorkingHours.value = workingHours ?: "No working hours found!"
        }
        firestoreCompanyDetails.loadCompanyWorkingHours(context) { geopoint ->
            companyWorkingHours.value = geopoint ?: "No location found!"
        }
    }

    Column(modifier = modifier.padding(16.dp))
    {
        Text(
            text = "Name: ${companyName.value}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Description: ${companyDescription.value}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Category: ${companyCategory.value}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Category: ${companyWorkingHours.value}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "Category: ${companyGeoPoint.value}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}