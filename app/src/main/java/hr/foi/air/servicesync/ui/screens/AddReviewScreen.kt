package hr.foi.air.servicesync.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.onSurfaceDark
import com.example.compose.onSurfaceLight
import hr.foi.air.servicesync.R
import hr.foi.air.servicesync.business.ReviewHandler
import hr.foi.air.servicesync.data.Review
import hr.foi.air.servicesync.ui.components.isDark

@Composable
fun AddReviewScreen(
    modifier: Modifier = Modifier,
    companyId: String,
    userId: String,
    navController: NavController,
    onReviewSubmit: (Boolean) -> Unit,
    reviewHandler: ReviewHandler = ReviewHandler()
) {
    val rating = remember { mutableStateOf(0) }
    val reviewText = remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.adding_review),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Start),
            color = isDark(onSurfaceDark, onSurfaceLight)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= rating.value) Icons.Filled.Star else Icons.Outlined.StarOutline,
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { rating.value = i }
                )
            }
        }
        OutlinedTextField(
            value = reviewText.value,
            onValueChange = { reviewText.value = it },
            label = { Text(text = stringResource(R.string.review_description)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            maxLines = 5
        )
        Button(
            onClick = {
                val review = Review(
                    companyId = companyId,
                    description = reviewText.value,
                    rating = rating.value,
                    userId = userId
                )

                reviewHandler.addReview(review) { success ->
                    onReviewSubmit(success)
                }
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.send_review))
        }
    }
}
