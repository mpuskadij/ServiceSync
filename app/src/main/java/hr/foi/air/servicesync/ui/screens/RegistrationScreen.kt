package hr.foi.air.servicesync.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.loginRegisterHandler

@Composable
fun RegistrationScreen(
    onRegisterClickSuccesfull: () -> Unit,
    onLoginClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold {innerPading ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPading),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 96.dp, max = 500.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    val isDarkTheme = isSystemInDarkTheme()
                    Box(modifier = Modifier)
                    {
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "ServiceSync logo",
                            modifier = Modifier
                                .size(280.dp)
                                .padding(8.dp),
                            colorFilter = if (isDarkTheme) {
                                ColorFilter.tint(Color.White, BlendMode.SrcIn)
                            } else {
                                ColorFilter.tint(Color.Black, BlendMode.SrcIn)
                            }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.registration),
                    style = MaterialTheme.typography.titleLarge,
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.email)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Lozinka") },
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick =  {
                        val activity = context as? android.app.Activity
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ActivityCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                // Request the permission
                                activity?.let {
                                    ActivityCompat.requestPermissions(
                                        it,
                                        arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                        101 // Request code
                                    )
                                }
                                return@Button
                            }
                        }
                        var checkedEmail:String
                        var checkedPassword:String
                        if(email==null||email==""){
                            checkedEmail = " "
                        }else{
                            checkedEmail= email
                        }
                        if(password==null||password==""){
                            checkedPassword = " "
                        }else{
                            checkedPassword= password
                        }
                        loginRegisterHandler().registerUser(checkedEmail, checkedPassword) { isRegistered ->
                            if (isRegistered) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.registration_successful),
                                    Toast.LENGTH_LONG
                                ).show()
                                onRegisterClickSuccesfull()
                            } else {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.registration_error),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp)
                ) {
                    Text(text = stringResource(R.string.registration))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.already_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { onLoginClick() },
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistrationScreen() {
//    RegistrationScreen()
}