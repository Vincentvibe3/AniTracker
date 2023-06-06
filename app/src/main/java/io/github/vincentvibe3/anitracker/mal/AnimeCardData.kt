package io.github.vincentvibe3.anitracker.mal

import androidx.compose.ui.graphics.painter.Painter

data class AnimeCardData(
    val title: String,
    var score:Float,
    var progress:Int,
    val totalEpisodes:Int,
    val image:ImageResource
)