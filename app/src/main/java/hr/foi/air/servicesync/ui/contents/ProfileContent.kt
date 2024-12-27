package hr.foi.air.servicesync.ui.contents

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.compose.onPrimaryDark
import com.example.compose.onPrimaryLight
import com.example.compose.primaryDark
import com.example.compose.primaryLight
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.LanguageChangeHelper
import hr.foi.air.servicesync.business.MapProviderManager
import hr.foi.air.servicesync.business.UserDataHandler
import hr.foi.air.servicesync.ui.components.MapDropdown
import hr.foi.air.servicesync.ui.components.isDark
import hr.foi.air.servicesync.ui.screens.ProfileInfoBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(modifier: Modifier = Modifier) {
    //Profile
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
    var mapProvider by remember { mutableStateOf("") }


    //Language
    val languageChangeHelper = LanguageChangeHelper()

    var languageSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var currentLanguage by remember { mutableStateOf(languageChangeHelper.getSelectedLanguage(context)) }
    val selectedLocale by remember { mutableStateOf(languageChangeHelper.getSelectedLocale(context)) }

    LaunchedEffect(currentLanguage) {
        languageChangeHelper.updateResources(context, currentLanguage)
    }

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
        mapProvider = MapProviderManager.getCurrentMapProviderName(context)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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
                    label = { Text(stringResource(R.string.first_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = surname,
                    onValueChange = { surname = it },
                    label = { Text(stringResource(R.string.last_name)) },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text(stringResource(R.string.username)) },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.profile_description)) },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text(stringResource(R.string.new_password)) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                MapDropdown(
                    mapProviderName = mapProvider,
                    onMapProviderChange = { newName ->
                        mapProvider = newName
                    }
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
                                    errorMessage = context.getString(R.string.error_saving_data)
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
                                    errorMessage = context.getString(R.string.error_saving_data)
                                }
                            }

                            userDataHandler.updatePassword(newPassword) { success ->
                                if (success) {
                                    showSuccessDialog = true
                                    isEditing = false
                                } else {
                                    showErrorDialog = true
                                    errorMessage = context.getString(R.string.error_saving_password)
                                }
                            }
                        }
                        newPasswordEmpty = false
                        MapProviderManager.saveMapProvider(
                            context = context,
                            mapProviderName = mapProvider
                        )
                    },
                    modifier = Modifier.width(125.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = isDark(primaryDark, primaryLight))
                ) {
                    Text(stringResource(R.string.save))
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.profile_image_default),  // Placeholder slika
                        contentDescription = stringResource(R.string.profile_image),
                        modifier = Modifier
                            .size(120.dp)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .align(Alignment.Center),
                        contentScale = ContentScale.Crop
                    )

                    TextButton(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .background(Color.Transparent),
                        onClick = {
                            languageSheet = true
                        }
                    ) {
                        Text(
                            text = selectedLocale.flagEmoji,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }


                if (languageSheet) {
                    ModalBottomSheet(
                        sheetState = sheetState,
                        onDismissRequest = { languageSheet = false },
                        content = {
                            languageChangeHelper.locales.forEach { locale ->
                                TextButton(
                                    onClick = {
                                        languageChangeHelper.setLanguage(context, locale.code)
                                        currentLanguage = locale.code
                                        (context as? Activity)?.recreate()
                                        languageSheet = false
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = locale.flagEmoji,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = locale.language,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                }
                            }
                        }
                    )
                }


                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { isEditing = true },
                    colors = ButtonDefaults.buttonColors(containerColor = isDark(primaryDark, primaryLight)),
                ) {
                    Text(
                        color = isDark(onPrimaryDark, onPrimaryLight),
                        style = MaterialTheme.typography.labelLarge,
                        text = stringResource(R.string.edit)
                    )
                }

                ProfileInfoBox(stringResource(R.string.full_name), "$name $surname")
                ProfileInfoBox(stringResource(R.string.username), "$username")
                ProfileInfoBox(stringResource(R.string.email), "$email")
                ProfileInfoBox(stringResource(R.string.profile_description), description)
                
                ProfileInfoBox(stringResource(R.string.map_type), mapProvider)
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text(stringResource(R.string.success)) },
            text = { Text(stringResource(R.string.successful_data_save)) },
            confirmButton = {
                TextButton(
                    onClick = { showSuccessDialog = false }
                ) {
                    Text(stringResource(R.string.close))
                }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Greška") },
            text = { Text("!!!!!!!: $errorMessage") },
            confirmButton = {
                TextButton(
                    onClick = { showErrorDialog = false }
                ) {
                    Text(stringResource(R.string.close))
                }
            }
        )
    }
}


