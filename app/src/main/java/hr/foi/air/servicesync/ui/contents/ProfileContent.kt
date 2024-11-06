package hr.foi.air.servicesync.ui.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.data.UserSession
import hr.foi.air.servicesync.ui.screens.ProfileInfoBox


@Composable
fun ProfileContent(modifier: Modifier = Modifier) {
    val firestore = FirebaseFirestore.getInstance()
    var userData by remember { mutableStateOf<Map<String, String>?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        firestore.collection("users")
            .document(UserSession.username)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userData = document.data?.mapValues { it.value as? String ?: "" }
                    isLoading = false
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_icon),  // Placeholder slika
            contentDescription = "Profilna slika",
            modifier = Modifier
                .size(120.dp)
                .padding(8.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        val name = userData?.get("name") ?: "N/A"
        val surname = userData?.get("surname") ?: "N/A"
        val email = userData?.get("email") ?: "N/A"

        ProfileInfoBox(
            label = "Ime i Prezime",
            value = "$name $surname"
        )
        ProfileInfoBox(label = "Email", value = email)
        ProfileInfoBox(label = "Lozinka", value = "********")

        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 4.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
