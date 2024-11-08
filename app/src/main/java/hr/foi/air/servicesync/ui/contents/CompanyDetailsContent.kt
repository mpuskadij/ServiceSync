package hr.foi.air.servicesync.ui.contents

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.ui.components.CompanyDescription
import hr.foi.air.servicesync.ui.components.CompanyLocation
import hr.foi.air.servicesync.ui.components.CompanyNameAndImage
import hr.foi.air.servicesync.ui.components.CompanyWorkingHours
import hr.foi.air.servicesync.ui.items.ProvidedServicesListItem
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
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier.fillMaxWidth().padding(WindowInsets.navigationBars.asPaddingValues()).verticalScroll(rememberScrollState())
    ) {
        val headlineModifier = Modifier.fillMaxWidth().padding(8.dp)
        val headlineTextStyle =  MaterialTheme.typography.headlineMedium

        CompanyNameAndImage("Tvrtka A",R.drawable.ic_launcher_background)

        Spacer(modifier = Modifier.size(50.dp))

        CompanyDescription("Opis vrtke A")

        Spacer(modifier = Modifier.size(25.dp))


        ListItem(headlineContent =  {
                Text(text = stringResource(id = R.string.services),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = headlineTextStyle,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp,)
                )

        }, supportingContent = {
            Column {
                //TODO new feature will require these list items to open a new screen when the icon > is clicked
                ProvidedServicesListItem("Servis A")
                ProvidedServicesListItem("Servis B")
            }
        },
        )

        CompanyWorkingHours()

        Spacer(Modifier.size(25.dp))

        CompanyLocation()

        Text(text = stringResource(R.string.reviews), style = headlineTextStyle,modifier  =headlineModifier)
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
}
@Preview
@Composable
fun PreviewCompanyDetails(){
    CompanyDetailsContent(2)
}