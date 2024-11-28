package hr.foi.air.servicesync.ui.components

import androidx.compose.animation.core.snap
import androidx.compose.foundation.Image
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.maps.interfaces.IMapProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import hr.foi.air.servicesync.abstracted.CompanyDetails
import mapproviders.GoogleMapProvider


class Company : CompanyDetails()
{
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    @Composable
    override fun GetCompanyDescription(description: String)
    {
        var companyDescription by remember { mutableStateOf<List<String>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit)
        {
            firebaseFirestore.collection("companies")
                .get()
                .addOnSuccessListener { snapshot ->
                    val descriptions = snapshot.documents.mapNotNull { it.getString("description") }
                    companyDescription = descriptions
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    println("Error fetching descriptions: ${exception.message}")
                    isLoading = false
                }
        }

        if (isLoading)
        {
            Text("Loading descriptions...")
        }
        else
        {
            CompanyDescriptionList(companyDescription)
        }

    }

    @Composable
    private fun CompanyDescriptionList(descriptions: List<String>)
    {
        androidx.compose.foundation.layout.Column {
            descriptions.forEach { description ->
                CompanyDescription(description = description)
            }
        }

    }


    @Composable
    override fun GetCompanyLocation(geoPoint: GeoPoint, mapProvider: IMapProvider)
    {
        var companyLocation by remember { mutableStateOf<GeoPoint?>(null) }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit)
        {
            firebaseFirestore.collection("companies")
                .get()
                .addOnSuccessListener { snapshot ->
                    companyLocation = snapshot.documents.firstOrNull()?.getGeoPoint("location")
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    println("Error fetching location: ${exception.message}")
                    isLoading = false
                }
        }

        if (isLoading)
        {
            Text("Loading location...")
        }
        else
        {
            companyLocation?.let { location ->
                (mapProvider as? GoogleMapProvider)?.CreateMap(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
            } ?: Text("Location not available")
        }

    }

    @Composable
    override fun GetCompanyCategory(companyCategory: String)
    {
        var category by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit)
        {
            firebaseFirestore.collection("companies")
                .get()
                .addOnSuccessListener { snapshot ->
                    category = snapshot.documents.firstOrNull()?.getString("category") ?: "Unknown"
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    println("Error fetching category: ${exception.message}")
                    isLoading = false
                }
        }

        if (isLoading)
        {
            Text("Loading category...")
        }
        else
        {
            Text("Category: $category")
        }

    }

    @Composable
    override fun GetCompanyName(companyName: String)
    {
        var name by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit)
        {
            firebaseFirestore.collection("companies")
                .get()
                .addOnSuccessListener { snapshot ->
                    name = snapshot.documents.firstOrNull()?.getString("name") ?: "No name available"
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    println("Error fetching name: ${exception.message}")
                    isLoading = false
                }
        }

        if (isLoading)
        {
            Text("Loading name...")
        }
        else
        {
            Text("Name: $name")
        }
    }

    @Composable
    override fun GetCompanyImage(pictureURL: String)
    {
        var imageUrl by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) {
            firebaseFirestore.collection("companies")
                .get()
                .addOnSuccessListener { snapshot ->
                    imageUrl = snapshot.documents.firstOrNull()?.getString("imageURL") ?: ""
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    println("Error fetching image URL: ${exception.message}")
                    isLoading = false
                }
        }

        if (isLoading) {
            Text("Loading image...")
        } else {
            if (imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null
                )
            } else {
                Text("No image available")
            }
        }
    }

    @Composable
    override fun GetCompanyWorkingHours(time: Int) {
        var workingHours by remember { mutableStateOf("") }
        var isLoading by remember { mutableStateOf(true) }

        LaunchedEffect(Unit)
        {
            firebaseFirestore.collection("companies")
                .get()
                .addOnSuccessListener { snapshot ->
                    workingHours = snapshot.documents.firstOrNull()?.getString("workingHours") ?: "Not available"
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    println("Error fetching working hours: ${exception.message}")
                    isLoading = false
                }
        }

        if (isLoading)
        {
            Text("Loading working hours...")
        }
        else
        {
            Text("Working hours: $workingHours")
        }
    }

}