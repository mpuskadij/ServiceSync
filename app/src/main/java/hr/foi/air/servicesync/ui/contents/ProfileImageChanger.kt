package hr.foi.air.servicesync.ui.contents

import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import com.google.firebase.auth.FirebaseAuth
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.backend.FirestoreUserDetails
import hr.foi.air.servicesync.business.encodeImageToBase64
import hr.foi.air.servicesync.business.uploadImageToImgur
import hr.foi.air.servicesync.ui.components.isDark


@Composable
fun ProfileImageChanger() {
    var profileImage by remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val userId = FirebaseAuth.getInstance().currentUser?.email ?: return
    val firestoreUserDetails = FirestoreUserDetails()

    LaunchedEffect(userId) {
        firestoreUserDetails.getProfileImageUrl(userId) { imageUrl ->
            profileImageUrl = imageUrl
        }
    }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            profileImage = bitmap.asImageBitmap()

            val encodedImage = encodeImageToBase64(bitmap)

            isLoading = true
            uploadImageToImgur(encodedImage, onSuccess = { imageUrl ->
                firestoreUserDetails.saveProfileImageUrlToFirestore(userId, imageUrl)
                profileImageUrl = imageUrl
                isLoading = false
            }, onFailure = { error ->
                errorMessage = error
                isLoading = false
            })
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row (
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
            Row (
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(0.dp, 0.dp, 14.dp, 0.dp),
            ){
                Box(modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = profileImageUrl ?: R.drawable.profile_image_default,
                        contentDescription = "Profile Image",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .border(2.dp, isDark(primaryDark, primaryLight), CircleShape)
                            .align(Alignment.Center),
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

        if (errorMessage.isNotEmpty()) {
            //Text(text = "Restart", color = Color.Red)
        }
    }
}