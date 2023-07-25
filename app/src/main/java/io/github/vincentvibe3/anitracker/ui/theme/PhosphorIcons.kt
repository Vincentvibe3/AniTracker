package io.github.vincentvibe3.anitracker.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import io.github.vincentvibe3.anitracker.R

object PhosphorIcons {

    @Composable fun smiley() =  ImageVector.vectorResource(id = R.drawable.smiley)
    @Composable fun smiley_meh() = ImageVector.vectorResource(id = R.drawable.smiley_meh)
    @Composable fun smiley_sad() = ImageVector.vectorResource(id = R.drawable.smiley_sad)

}