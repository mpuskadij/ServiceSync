package hr.foi.air.servicesync.ui.contents

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.onSurfaceVariantDark
import com.example.compose.onSurfaceVariantLight
import com.example.compose.surfaceContainerHighDark
import com.example.compose.surfaceContainerHighLight
import com.google.firebase.firestore.FirebaseFirestore
import hr.foi.air.servicesync.ui.components.CompanyCard
import hr.foi.air.servicesync.ui.components.isDark

@Composable
fun SearchContent(modifier: Modifier = Modifier, navController: NavController)
{
    val db = FirebaseFirestore.getInstance()
    val companyNames = remember { mutableStateOf<List<Pair<String, String?>>>(emptyList()) }
    val companyCategory = remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    val filteredCompany = remember { mutableStateOf<List<Pair<String, String?>>>(emptyList()) }
    val distinctCategories = remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf(TextFieldValue(""))}

    LaunchedEffect(Unit) {
        db.collection("companies")
            .get()
            .addOnSuccessListener { documents ->
                val companies = documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: "Unknown"
                    val imageUrl = doc.getString("pictureURL")
                    if (name.isNotEmpty()) name to imageUrl else null
                }

                val categories = documents.mapNotNull { doc ->
                    val name = doc.getString("name") ?: "Unknown"
                    val category = doc.getString("category") ?: "Unknown"
                    if (name.isNotEmpty() && category.isNotEmpty()) name to category else null
                }

                companyNames.value = companies
                companyCategory.value = categories
                filteredCompany.value = companies

                distinctCategories.value = categories.map { it.second }.distinct()
                isLoading.value = false
            }
            .addOnFailureListener {
                isLoading.value = false
            }
    }

    LaunchedEffect(searchQuery.text, selectedCategory)
    {
        filteredCompany.value = companyNames.value.filter { company ->
            val categoryMatch = selectedCategory?.let { category ->
                companyCategory.value.any{
                    it.first == company.first && it.second == category
                }
            } ?: true

            company.first.contains(searchQuery.text, ignoreCase = true) && categoryMatch
        }
    }

    val combinedCompanyData = filteredCompany.value.map { company ->
        val category = companyCategory.value.find { it.first == company.first }?.second ?: "Unknown"
        company.first to (company.second to category)
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
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
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

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(distinctCategories.value) { category ->
                val isSelected = category == selectedCategory
                val backgroundColor = if (isSelected)
                {
                    isDark(onSurfaceVariantDark, onSurfaceVariantLight)
                }
                else
                {
                    isDark(surfaceContainerHighDark, surfaceContainerHighLight)
                }
                val textColor = if (isSelected)
                {
                    isDark(onSurfaceLight, onSurfaceDark)
                }
                else
                {
                    isDark(onSurfaceDark, onSurfaceLight)
                }

                Text(
                    text = category,
                    color = textColor,
                    modifier = Modifier
                        .padding(4.dp)
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clickable {
                            selectedCategory = if (isSelected) null else category
                        }
                )
            }
        }

        if (isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredCompany.value) { (companyName, imageUrl) ->
                    val companyCategory = companyCategory.value
                        .find { it.first == companyName }
                        ?.second ?: "Unknown"

                    CompanyCard(
                        companyName = companyName,
                        imageUrl = imageUrl,
                        companyCategory = companyCategory,
                        onCardClick = {
                            navController.navigate("company/$companyName")
                        }
                    )
                }
            }
        }
    }
}

