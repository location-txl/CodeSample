package com.location.codesample.composeCommon

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.selects.select


@Composable
fun SelectView(modifier: Modifier = Modifier, text: String, selected: Boolean, onSelect: () -> Unit) {
    Box(
        modifier
            .background(
                if (selected) androidx.compose.ui.graphics.Color.Blue
                else androidx.compose.ui.graphics.Color.White
            )
            .clickable {
                onSelect()
            }, contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = text,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            color = if (selected) androidx.compose.ui.graphics.Color.White
            else androidx.compose.ui.graphics.Color.Black
        )
    }
}