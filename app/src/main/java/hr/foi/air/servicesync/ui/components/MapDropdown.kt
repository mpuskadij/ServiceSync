package hr.foi.air.servicesync.ui.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.maps.interfaces.IMapProvider
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.MapProviderManager
import java.util.ServiceLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapDropdown(mapProviderName: String,onMapProviderChange : (String) -> Unit) {
    val  mapProviders = MapProviderManager.getAllProviders()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {expanded = !expanded},
        modifier = Modifier
    ){
        var chosenMapProvider by remember { mutableStateOf(mapProviderName)
         }
        TextField(
            label = {
                Text(text = stringResource(R.string.choose_map_type)) },
            value = chosenMapProvider,
            onValueChange = {
                chosenMapProvider = it
            },
            readOnly = true,
            enabled = false,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.fillMaxWidth().menuAnchor(MenuAnchorType.PrimaryNotEditable, expanded).clickable { expanded  =true }
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
                        onMapProviderChange(chosenMapProvider)
                    }
                )
            }
        }
    }
}