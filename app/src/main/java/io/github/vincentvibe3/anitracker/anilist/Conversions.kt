package io.github.vincentvibe3.anitracker.anilist

import io.github.vincentvibe3.anitraklib.anilist.types.MediaFormat
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus
import io.github.vincentvibe3.anitraklib.anilist.types.MediaSource
import io.github.vincentvibe3.anitraklib.anilist.types.MediaStatus

fun MediaSource.toPrettyString():String {
    return when (this){
        MediaSource.MANGA -> "Manga"
        MediaSource.ORIGINAL -> "Original"
        MediaSource.LIGHT_NOVEL -> "Light Novel"
        MediaSource.VISUAL_NOVEL -> "Visual Novel"
        MediaSource.VIDEO_GAME -> "Video Game"
        MediaSource.OTHER -> "Other"
        MediaSource.NOVEL -> "Novel"
        MediaSource.DOUJINSHI -> "Doujinshi"
        MediaSource.ANIME -> "Anime"
        MediaSource.WEB_NOVEL -> "Web Novel"
        MediaSource.LIVE_ACTION -> "Live Action"
        MediaSource.GAME -> "Game"
        MediaSource.COMIC -> "Comic"
        MediaSource.MULTIMEDIA_PROJECT -> "Multimedia Project"
        MediaSource.PICTURE_BOOK -> "Picture Book"
    }
}

fun MediaListStatus.toPrettyString():String{
    return when (this){
        MediaListStatus.COMPLETED -> "Completed"
        MediaListStatus.CURRENT -> "Watching"
        MediaListStatus.PLANNING -> "Plan To Watch"
        MediaListStatus.DROPPED -> "Dropped"
        MediaListStatus.PAUSED -> "On Hold"
        MediaListStatus.REPEATING -> "Watching"
    }
}

fun MediaFormat?.toPrettyString(): String? {
    return when (this){
        MediaFormat.TV -> "TV"
        MediaFormat.TV_SHORT -> "TV Short"
        MediaFormat.MOVIE -> "Movie"
        MediaFormat.SPECIAL -> "Special"
        MediaFormat.OVA -> "OVA"
        MediaFormat.ONA -> "ONA"
        MediaFormat.MUSIC -> "Music"
        MediaFormat.MANGA -> "Manga"
        MediaFormat.NOVEL -> "Novel"
        MediaFormat.ONE_SHOT -> "One Shot"
        null -> null
    }
}