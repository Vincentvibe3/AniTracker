package io.github.vincentvibe3.anitracker.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun QuickAccessButton(onClick:()->Unit, icon: ImageVector, enabled:Boolean=true){
    FilledIconButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = IconButtonDefaults.filledIconButtonColors(Color(0xFFF0F0F0)),
        enabled = enabled
    ) {
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = icon,
            contentDescription = ""
        )
    }
}