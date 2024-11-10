package hr.foi.air.servicesync.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.foi.air.servicesync.business.loginRegisterHandler
import hr.foi.air.servicesync.ui.components.isDark

@Composable
fun LoginScreen(
    onLoginClickSuccesfull: () -> Unit,
    onRegistrationClick: () -> Unit,
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                    Text(
                        text = "ServiceSync",
                        style = MaterialTheme.typography.displayLarge,
                    )
                    Text(
                        text = "Sve na jednom mjestu",

                        )
                }
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Prijava",
                    style = MaterialTheme.typography.titleLarge,
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
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
                        loginRegisterHandler().loginUser(checkedEmail, checkedPassword) { isLoggedIn ->
                            if (isLoggedIn) {
                                Toast.makeText(
                                    context,
                                    "Prijava je uspjesna.",
                                    Toast.LENGTH_LONG
                                ).show()
                                onLoginClickSuccesfull()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Prijava nije uspjesna.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = isDark(Color(0xFF8FA7D6), Color(0xFF65558F)))
                ) {
                    Text(text = "Prijava")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Nemate račun? Registrirajte se",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable { onRegistrationClick()},
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
fun PreviewLoginScreen() {
//    LoginScreen()
}
