package hr.foi.air.servicesync.business

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import com.example.compose.onSurfaceVariantDark
import com.example.compose.onSurfaceVariantLight
import com.example.compose.surfaceContainerHighDark
import com.example.compose.surfaceContainerHighLight
import hr.foi.air.servicesync.ui.components.isDark

class CategoryHandler
{
    @Composable
    fun getCategoryColor(
        category: String,
        selectedCategory: String?
    ): Triple<Boolean, Color, Color>
    {
        val isSelected = category == selectedCategory
        val backgroundColor = if (isSelected)
        {
            isDark(onSurfaceVariantDark, onSurfaceVariantLight)
        }
        else
        {
            isDark(surfaceContainerHighDark, surfaceContainerHighLight)
        }
        val textColor = if (isSelected)
        {
            isDark(onSurfaceLight, onSurfaceDark)
        }
        else
        {
            isDark(onSurfaceDark, onSurfaceLight)
        }
        return Triple(isSelected, backgroundColor, textColor)
    }
}