package io.github.vincentvibe3.anitracker.anilist

import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus
import kotlinx.datetime.LocalDate

data class AnimeCardData(
    val title: String,
    var score:Float,
    var progress:Int,
    val totalEpisodes:Int,
    val imageUrl:String,
    val id:Int,
    var status: MediaListStatus,
    var rewatches:Int,
    val lists:MutableList<String>,
    var customLists:Map<String, Boolean>,
    var notes:String?,
    var private:Boolean,
    var favourite:Boolean,
    var startedAt:LocalDate?,
    var completedAt:LocalDate?
)