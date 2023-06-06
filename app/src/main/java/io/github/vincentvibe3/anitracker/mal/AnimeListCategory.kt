package io.github.vincentvibe3.anitracker.mal

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class AnimeListCategory(
    val name:String,
    val type:Categories,
    val index:Int,
) {
    val backingList = mutableStateListOf<AnimeCardData>()

    fun isEmpty(): Boolean {
        return backingList.isEmpty()
    }
}