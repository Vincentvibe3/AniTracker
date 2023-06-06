package io.github.vincentvibe3.anitracker.mal

import androidx.compose.runtime.collection.mutableVectorOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AnimeList(
    val watching: SnapshotStateList<AnimeCardData>,
    val completed: SnapshotStateList<AnimeCardData>
)

class AnimeListViewModel:ViewModel() {

    private val _lists = mutableStateMapOf(
        "Watching" to AnimeListCategory("Watching", Categories.WATCHING, 0),
        "Completed" to AnimeListCategory("Completed", Categories.COMPLETED, 1)
    )

    val uiState
        get() =  _lists.toSortedMap{ value1, value2 ->
            _lists[value1]!!.index-_lists[value2]!!.index
        }

    fun getList(name:String): AnimeListCategory? {
        return _lists[name]
    }

    fun moveTo(entry:AnimeCardData, src:String, destination:String){
        _lists[src]?.backingList?.remove(entry)
        _lists[destination]?.backingList?.add(entry)
    }

}