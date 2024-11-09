package hr.foi.air.servicesync.ui.contents

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.GeoPoint
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.backend.FirestoreCompanyDetails
import hr.foi.air.servicesync.ui.components.CompanyDescription
import hr.foi.air.servicesync.ui.components.CompanyNameAndImage
import hr.foi.air.servicesync.ui.items.ProvidedServicesListItem

@Composable
fun CompanyDetailsContent(
    modifier: Modifier = Modifier,
    context: Context? = null
) {
    val firestoreCompanyDetails = FirestoreCompanyDetails()

    val companyName = remember { mutableStateOf("Loading...") }
    val companyDescription = remember { mutableStateOf("Loading...") }
    val companyCategory = remember { mutableStateOf("Loading...") }
    val companyWorkingHours = remember { mutableStateOf(0) }
    val companyGeoPoint = remember { mutableStateOf(GeoPoint(0.0, 0.0)) }

    if (context != null) {
        LaunchedEffect(Unit) {
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
                companyWorkingHours.value = workingHours ?: 0
            }
            firestoreCompanyDetails.loadCompanyGeopoint(context) { geopoint ->
                companyGeoPoint.value = geopoint ?: GeoPoint(0.0, 0.0)
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .verticalScroll(rememberScrollState())
    ) {
        val headlineModifier = Modifier.fillMaxWidth().padding(8.dp)
        val headlineTextStyle = MaterialTheme.typography.headlineMedium

        CompanyNameAndImage(companyName.value, R.drawable.ic_launcher_background)
        Spacer(modifier = Modifier.size(50.dp))

        CompanyDescription(companyDescription.value)
        Spacer(modifier = Modifier.size(25.dp))

        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.services),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = headlineTextStyle,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                )
            },
            supportingContent = {
                Column {
                    ProvidedServicesListItem(companyCategory.value)
                }
            },
        )

        Text(
            text = "Working Hours: ${companyWorkingHours.value}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )
        Spacer(Modifier.size(25.dp))

        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(id = R.string.location),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = headlineTextStyle,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
                )
            },
            supportingContent = {
                Column {
                    ProvidedServicesListItem(companyGeoPoint.value.toString())
                }
            },
        )


        Text(text = stringResource(R.string.reviews), style = headlineTextStyle, modifier = headlineModifier)
    }
}
