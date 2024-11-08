package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import hr.foi.air.servicesync.backend.FirestoreCompanyDetails
import hr.foi.air.servicesync.ui.contents.CompanyDetailsContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val firestoreCompanyDetails = FirestoreCompanyDetails()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Pretraži", style = MaterialTheme.typography.titleLarge) },
            )
        },
        modifier = modifier
    ) { innerPadding ->
        CompanyDetailsContent(
            modifier = Modifier.padding(innerPadding),
            firestoreCompanyDetails = firestoreCompanyDetails,
            context = context
        )
    }
}