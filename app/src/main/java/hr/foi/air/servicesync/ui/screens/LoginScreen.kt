package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize(),
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
                onClick =  { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 64.dp)
            ) {
                Text(text = "Prijava")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Nemate račun? Registrirajte se",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.clickable { onLoginClick() },
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    LoginScreen(onLoginClick = { /* Logika za registraciju */ })
}
