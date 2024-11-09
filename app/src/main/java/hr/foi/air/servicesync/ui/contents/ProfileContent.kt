package hr.foi.air.servicesync.ui.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.UserDataHandler
import hr.foi.air.servicesync.ui.screens.ProfileInfoBox

@Composable
fun ProfileContent(modifier: Modifier = Modifier) {
    val userDataHandler = UserDataHandler()
    var userData by remember { mutableStateOf<Map<String, String>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("")}
    var description by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var newPasswordEmpty by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        userDataHandler.loadUserDetails { data ->
            userData = data
            name = data?.get("name") ?: ""
            surname = data?.get("surname") ?: ""
            username = data?.get("username") ?: ""
            email = data?.get("email") ?: ""
            description = data?.get("description") ?: ""
            isLoading = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (isEditing) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Ime") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text("Prezime") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Korisničko ime/tag") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Opis") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nova Lozinka") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (newPassword.isEmpty()) {
                            newPasswordEmpty = true

                            userDataHandler.saveUserDetails(
                                name = name,
                                surname = surname,
                                username = username,
                                description = description
                            ) { success ->
                                if (success) {
                                    showSuccessDialog = true
                                    isEditing = false
                                } else {
                                    showErrorDialog = true
                                    errorMessage = "Greška pri spremanju podataka."
                                }
                            }
                        } else {
                            userDataHandler.saveUserDetails(
                                name = name,
                                surname = surname,
                                username = username,
                                description = description
                            ) { success ->
                                if (success) {
                                    showSuccessDialog = true
                                    isEditing = false
                                } else {
                                    showErrorDialog = true
                                    errorMessage = "Greška pri spremanju podataka."
                                }
                            }

                            userDataHandler.updatePassword(newPassword) { success ->
                                if (success) {
                                    showSuccessDialog = true
                                    isEditing = false
                                } else {
                                    showErrorDialog = true
                                    errorMessage = "Greška pri promjeni lozinke. Ponovno se prijavite!"
                                }
                            }
                        }
                        newPasswordEmpty = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Spremi")
                }
            } else {
                Image(
                    painter = painterResource(id = R.drawable.profile_image_default),  // Placeholder slika
                    contentDescription = "Profilna slika",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))


                ProfileInfoBox("Ime i Prezime", "$name $surname")
                ProfileInfoBox("Korisničko ime/tag", "$username")
                ProfileInfoBox("Email", "$email")
                ProfileInfoBox("Opis", description)

                Button(onClick = { isEditing = true }) {
                    Text("Uredi")
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Uspješno") },
            text = { Text("Vaši podaci su uspješno ažurirani.") },
            confirmButton = {
                TextButton(
                    onClick = { showSuccessDialog = false }
                ) {
                    Text("Zatvori")
                }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Greška") },
            text = { Text("Došlo je do pogreške: $errorMessage") },
            confirmButton = {
                TextButton(
                    onClick = { showErrorDialog = false }
                ) {
                    Text("Zatvori")
                }
            }
        )
    }
}


