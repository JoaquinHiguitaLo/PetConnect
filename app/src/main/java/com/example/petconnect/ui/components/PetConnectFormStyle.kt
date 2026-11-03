
package com.example.petconnect.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

object PetConnectFormStyle {
    val background = Color(0xFFFAF9F6)
    val primary = Color(0xFF227C78)
    val text = Color(0xFF303534)
    val border = Color(0xFFD9DEDC)

    val horizontalPadding = 24.dp
    val fieldRadius = 14.dp
    val buttonRadius = 16.dp
    val buttonHeight = 48.dp
    const val buttonWidthFraction = 0.72f
}

@Composable
fun PetConnectFormTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier.padding(
            horizontal = PetConnectFormStyle.horizontalPadding
        ),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
        color = PetConnectFormStyle.primary
    )
}

@Composable
fun PetConnectFormLabel(
    text: String,
    required: Boolean = false
) {
    Row {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = PetConnectFormStyle.text
        )

        if (required) {
            Text(
                text = " *",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFFE05D4F)
            )
        }
    }
}
