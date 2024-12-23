package hr.foi.air.servicesync.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.maps.interfaces.IMapProvider
import hr.foi.air.servicesync.R
import java.util.ServiceLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapDropdown() {
    val mapProviders by lazy {
        ServiceLoader.load(IMapProvider::class.java).toList()
    }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = it},
        modifier = Modifier.fillMaxWidth()
    ){
        val noProviders = stringResource(R.string.no_providers_found)
        var chosenMapProvider by remember { mutableStateOf(mapProviders.firstOrNull()?.getName() ?: noProviders
        ) }
        TextField(
            label = {
                Text(text = stringResource(R.string.choose_map_type)) },
            value = chosenMapProvider,
            onValueChange = {
                chosenMapProvider = it
            },
            enabled = false

        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = {expanded = false}) {
            mapProviders.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = it.getName())
                    },
                    onClick = {
                        expanded = false
                        chosenMapProvider = it.getName()
                    }
                )
            }
        }
    }
}