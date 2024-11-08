package hr.foi.air.servicesync.ui.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchContent(modifier: Modifier = Modifier)
{
    Column() {
        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = {},
        ) {
            Text(text = "Company details")
        }
    }
}