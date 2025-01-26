package hr.foi.air.servicesync.ui.contents

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import com.google.firebase.auth.FirebaseAuth
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.ImageProcessor
import hr.foi.air.servicesync.business.UserDataHandler
import hr.foi.air.servicesync.ui.components.isDark


@Composable
fun ProfileImageChanger() {
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.email ?: return
    val imageProcessor = ImageProcessor(context)
    val userDataHandler = UserDataHandler()

    LaunchedEffect(userId) {
        userDataHandler.fetchProfileImageUrl { imageUrl ->
            profileImageUrl = imageUrl
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            isLoading = true
            imageProcessor.processImage(
                uri = uri,
                onSuccess = { imageUrl ->
                    userDataHandler.saveProfileImage(userId, imageUrl)
                    userDataHandler.fetchProfileImageUrl { imageUrl ->
                        profileImageUrl = imageUrl
                    }
                    isLoading = false
                },
                onFailure = { error ->
                    errorMessage = error
                    isLoading = false
                }
            )
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = { launcher.launch("image/*") }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_add_photo_alternate_24),
                    contentDescription = "Change photo",
                    modifier = Modifier.size(24.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(0.dp, 0.dp, 14.dp, 0.dp),
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = profileImageUrl ?: R.drawable.profile_image_default,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(170.dp)
                            .clip(CircleShape)
                            .border(2.dp, isDark(primaryDark, primaryLight), CircleShape)
                            .align(Alignment.Center)
                            .clickable { showDialog = true },
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.profile_icon),
                        error = painterResource(id = R.drawable.baseline_access_time_filled_24)
                    )
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
            }
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { (R.string.profile_image) },
            text = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = profileImageUrl ?: R.drawable.profile_image_default,
                        contentDescription = "Larger Profile Image",
                        modifier = Modifier.size(300.dp),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(id = R.drawable.profile_icon),
                        error = painterResource(id = R.drawable.baseline_access_time_filled_24)
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}