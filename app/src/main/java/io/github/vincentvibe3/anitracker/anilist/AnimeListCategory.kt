package io.github.vincentvibe3.anitracker.anilist

import androidx.compose.runtime.mutableStateListOf
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus


data class AnimeListCategory(
    val name:String,
    val type: MediaListStatus,
    val index:Int,
) {
    val backingList = mutableStateListOf<AnimeCardData>()

    fun isEmpty(): Boolean {
        return backingList.isEmpty()
    }
}