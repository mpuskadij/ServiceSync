package hr.foi.air.servicesync.ui.contents

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.onSurfaceVariantDark
import com.example.compose.onSurfaceVariantLight
import com.example.compose.surfaceContainerHighDark
import com.example.compose.surfaceContainerHighLight
import com.google.firebase.firestore.FirebaseFirestore
import hr.foi.air.servicesync.ui.components.isDark

@Composable
fun SearchContent(modifier: Modifier = Modifier, navController: NavController)
{
    val db = FirebaseFirestore.getInstance()
    val companyNames = remember { mutableStateOf<List<Pair<String, String?>>>(emptyList()) }
    val filteredCompanyName = remember { mutableStateOf<List<Pair<String, String?>>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf(TextFieldValue(""))}

    LaunchedEffect(Unit)
    {
        db.collection("companies")
            .get()
            .addOnSuccessListener { documents ->
                val companies = documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: "Unknown"
                    val imageUrl = doc.getString("pictureURL")
                    if (name.isNotEmpty())
                    {
                        name to imageUrl
                    }
                    else
                    {
                        null
                    }
                }

                Log.d("SearchContent", "Fetched companies: ${companies}")

                companyNames.value = companies
                filteredCompanyName.value = companies
                isLoading.value = false
            }
            .addOnFailureListener { exception ->
                Log.e("SearchContent", "Error fetching companies", exception)
                isLoading.value = false
            }
    }

    LaunchedEffect(searchQuery.text)
    {
        filteredCompanyName.value = if (searchQuery.text.isEmpty())
        {
            companyNames.value
        }
        else
        {
            val filteredList = companyNames.value.filter {
                it.first.contains(searchQuery.text, ignoreCase = true)
            }
            Log.d("SearchContent", "FilteredCompanies: $filteredList")
            filteredList
        }

        Log.d("SearchContent", "Filtered companies: ${filteredCompanyName.value}")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TextField(
            value = searchQuery.text,
            onValueChange = { newText: String ->
                searchQuery = searchQuery.copy(text = newText)
                Log.d("SearchContent", "Search query updated: ${newText}")
            },
            placeholder = {
                Text(
                    text = "Pretraživanje",
                    color = isDark(onSurfaceVariantDark, onSurfaceVariantLight)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = isDark(onSurfaceVariantDark, onSurfaceVariantLight)
                )
            },
            textStyle = TextStyle(color = isDark(onSurfaceVariantDark, onSurfaceVariantLight)),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    color = isDark(surfaceContainerHighDark, surfaceContainerHighLight),
                    shape = RoundedCornerShape(28.dp)
                )
                .padding(horizontal = 16.dp),
            colors = TextFieldDefaults.colors(
                cursorColor = isDark(onSurfaceDark, onSurfaceLight),
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            singleLine = true
        )

        if (isLoading.value)
        {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        else
        {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredCompanyName.value) { (companyName, imageUrl) ->
                    CompanyCard(
                        companyName = companyName,
                        imageUrl = imageUrl,
                        onCardClick = {
                            navController.navigate("company/$companyName")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CompanyCard(
    companyName: String,
    imageUrl: String?,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onCardClick
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            imageUrl?.let {
                AsyncImage(
                    model = it,
                    contentDescription = "$companyName background",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer
                    {
                        alpha = 0.8f
                    }
                    .background(Color.Black.copy(alpha = 0.6f))
            ) {
                Text(
                    text = companyName,
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
