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
fun QuickAccessButton(onClick:()->Unit, icon: @Composable ()->Unit, modifier:Modifier=Modifier, enabled:Boolean=true, color:Color=Color(0xFFF0F0F0)){
    FilledIconButton(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = IconButtonDefaults.filledIconButtonColors(color),
        enabled = enabled,
        modifier = Modifier.then(modifier)
    ) {
        icon()
    }
}

@Composable
fun QuickAccessButton(onClick:()->Unit, icon: ImageVector, modifier:Modifier=Modifier, enabled:Boolean=true, color:Color=Color(0xFFF0F0F0)){
    QuickAccessButton(onClick = onClick, icon = {
        Icon(imageVector = icon, contentDescription = null, modifier=Modifier.size(16.dp))
    }, modifier, enabled, color)
}