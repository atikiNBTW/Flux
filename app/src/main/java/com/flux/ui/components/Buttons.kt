package com.flux.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.ViewAgenda
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CloseButton(onCloseClicked:  () -> Unit) {
    IconButton(onClick = onCloseClicked) {
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = "Close",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SettingsButton(onSettingsClicked: () -> Unit) {
    IconButton(onClick = onSettingsClicked) {
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = "Settings",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun GridViewButton(isGridView: Boolean, onButtonClicked: () -> Unit) {
    IconButton(onClick = onButtonClicked) {
        Icon(
            imageVector = if(isGridView) Icons.Outlined.GridView else Icons.Outlined.ViewAgenda,
            contentDescription = "GridView",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}