package hr.foi.air.servicesync.ui.contents

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.onSurfaceVariantDark
import com.example.compose.onSurfaceVariantLight
import com.example.compose.outlineDark
import com.example.compose.outlineLight
import com.example.compose.surfaceContainerHighDark
import com.example.compose.surfaceContainerHighLight
import com.example.compose.surfaceContainerLowDark
import com.example.compose.surfaceContainerLowLight
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.CategoryHandler
import hr.foi.air.servicesync.business.CompanyDetailsHandler
import hr.foi.air.servicesync.business.ReviewHandler
import hr.foi.air.servicesync.ui.components.CompanyCard
import hr.foi.air.servicesync.ui.components.isDark

@Composable
fun SearchContent(modifier: Modifier = Modifier, navController: NavController, onQRCameraClick: () -> Unit)
{
    val reviewsHandler = ReviewHandler()
    val companyService = CompanyDetailsHandler()
    val getCategoryColor = CategoryHandler()

    val companyNames = remember { mutableStateOf<List<Pair<String, String?>>>(emptyList()) }
    val companyCategory = remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    val reviewAverages = remember { mutableStateOf(mapOf<String, Double>()) }
    val companyCities = remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    val filteredCompany = remember { mutableStateOf<List<Pair<String, String?>>>(emptyList()) }

    val distinctCategories = remember { mutableStateOf<List<String>>(emptyList()) }
    val distinctCities = remember { mutableStateOf<List<String>>(emptyList()) }

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var selectedCity by remember { mutableStateOf<String?>(null) }

    val isLoading = remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var showCityDropdown by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val chooseCityText = stringResource(R.string.choose_city)
    val cityTextFieldState = remember { mutableStateOf(TextFieldValue(chooseCityText)) }


    LaunchedEffect(Unit)
    {
        companyService.getCompanyData(
            onSuccess = { companies, categories, cities, distinctCityList ->
                companyNames.value = companies
                companyCategory.value = categories
                companyCities.value = cities
                filteredCompany.value = companies
                distinctCategories.value = categories.map { it.second }.distinct()
                distinctCities.value = distinctCityList
                isLoading.value = false
            },
            onFailure = {
                isLoading.value = false
            }
        )
    }

    LaunchedEffect(searchQuery.text, selectedCategory, selectedCity)
    {
        filteredCompany.value = companyNames.value.filter { company ->
            val categoryMatch = selectedCategory?.let { category ->
                companyCategory.value.any {
                    it.first == company.first && it.second == category
                }
            } ?: true

            val cityMatch = selectedCity?.let { city ->
                companyCities.value.any {
                    it.first == company.first && it.second == city
                }
            } ?: true

            company.first.contains(searchQuery.text, ignoreCase = true) && categoryMatch && cityMatch
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        TextField(
            value = searchQuery.text,
            onValueChange = { newText ->
                searchQuery = searchQuery.copy(text = newText)
                Log.d("SearchContent", "Search query updated: $newText")
            },
            placeholder = {
                Text(
                    text = stringResource(R.string.searching),
                    color = isDark(onSurfaceVariantDark, onSurfaceVariantLight)
                )
            },
            trailingIcon = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = isDark(onSurfaceVariantDark, onSurfaceVariantLight)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.qr_code_scanner),
                        contentDescription = "QR Code Scanner Icon",
                        tint = isDark(onSurfaceVariantDark, onSurfaceVariantLight),
                        modifier = Modifier.clickable {
                            onQRCameraClick()
                        }
                    )
                }
            },
            textStyle = TextStyle(color = isDark(onSurfaceVariantDark, onSurfaceVariantLight)),
            modifier = Modifier
                .padding(16.dp, 16.dp, 16.dp, 0.dp)
                .fillMaxWidth(0.95f)
                .align(Alignment.CenterHorizontally)
                .height(56.dp)
                .zIndex(1f)
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

        Text(
            modifier = Modifier.padding(20.dp, 16.dp, 20.dp, 0.dp),
            text = stringResource(R.string.services),
            style = MaterialTheme.typography.headlineMedium,
            color = isDark(onSurfaceDark, onSurfaceLight)
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .clipToBounds()
                .padding(0.dp, 12.dp, 0.dp, 8.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(distinctCategories.value) { category ->
                val (isSelected, backgroundColor, textColor) = getCategoryColor.getCategoryColor(
                    category,
                    selectedCategory
                )

                Text(
                    text = category,
                    color = textColor,
                    modifier = Modifier
                        .padding(0.dp)
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .clickable {
                            selectedCategory = if (isSelected) null else category
                        }
                )
            }
        }

        Spacer(modifier = Modifier.padding(bottom = 6.dp, top = 0.dp))

        when {
            isLoading.value -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .heightIn(300.dp)
                .pointerInput(Unit) {}
                .padding(16.dp, 5.dp, 16.dp, 0.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            item {
                Card(
                    shape = AbsoluteRoundedCornerShape(4.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .fillMaxWidth()
                        .wrapContentSize(Alignment.TopStart),
                    border = BorderStroke((0.5).dp, isDark(outlineDark, outlineLight)),
                    colors = CardColors(
                        contentColor = isDark(onSurfaceDark, onSurfaceLight),
                        containerColor = Color.Transparent,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                    )
                ) {
                    TextField(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(vertical = 4.dp),
                        value = cityTextFieldState.value,
                        onValueChange = { newValue ->
                            cityTextFieldState.value = newValue
                            showCityDropdown = true
                        },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown Icon") },
                        singleLine = true,
                        readOnly = true,
                        enabled = true,
                        textStyle = MaterialTheme.typography.bodyMedium,
                        colors = TextFieldDefaults.colors(
                            cursorColor = isDark(onSurfaceDark, onSurfaceLight),
                            unfocusedContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent
                        ),
                    )
                    DropdownMenu(
                        border = BorderStroke((0.5).dp, isDark(outlineDark, outlineLight)),
                        containerColor = isDark(surfaceContainerLowDark, surfaceContainerLowLight),
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .align(Alignment.Start),
                        expanded = showCityDropdown,
                        onDismissRequest = { showCityDropdown = false },
                        scrollState = scrollState
                    ) {
                        DropdownMenuItem(
                            modifier = Modifier
                                .fillParentMaxWidth(),
                            text = {
                                Text(stringResource(R.string.choose_city))
                            },
                            onClick = {
                                selectedCity = null
                                cityTextFieldState.value = TextFieldValue(chooseCityText)
                                showCityDropdown = false
                            }
                        )
                        distinctCities.value.forEach { city ->
                            DropdownMenuItem(
                                modifier = Modifier
                                    .fillParentMaxWidth(),
                                text = {
                                    Text(city)
                                },
                                onClick = {
                                    selectedCity = city
                                    cityTextFieldState.value = TextFieldValue(city)
                                    showCityDropdown = false
                                }
                            )
                        }
                    }
                    LaunchedEffect(showCityDropdown)
                    {
                        if (showCityDropdown)
                        {
                            scrollState.scrollTo(scrollState.maxValue)
                        }
                    }
                }
            }
            items(filteredCompany.value) { (companyName, imageUrl) ->
                val companyCategory = companyCategory.value
                    .find { it.first == companyName }
                    ?.second ?: "Unknown"

                val averageRating = reviewAverages.value[companyName] ?: "Loading..."

                if (!reviewAverages.value.containsKey(companyName)) {
                    reviewsHandler.fetchAndCalculateReviewAverage(companyName) { average ->
                        reviewAverages.value += (companyName to average)
                    }
                }

                CompanyCard(
                    companyName = companyName,
                    imageUrl = imageUrl,
                    companyCategory = companyCategory,
                    companyRating = averageRating.toString(),
                    onCardClick = {
                        navController.navigate("company/$companyName") {
                            popUpTo("search") { inclusive = false }
                        }
                    }
                )
            }
        }
    }
}