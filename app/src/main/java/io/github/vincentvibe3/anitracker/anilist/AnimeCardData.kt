package io.github.vincentvibe3.anitracker.anilist

import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus

data class AnimeCardData(
    val title: String,
    var score:Float,
    var progress:Int,
    val totalEpisodes:Int,
    val imageUrl:String,
    val id:Int,
    var status: MediaListStatus,
    val rewatches:Int,
    val notes:String?,
)