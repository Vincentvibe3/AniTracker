package io.github.vincentvibe3.anitracker.anilist

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import io.github.vincentvibe3.anitraklib.anilist.serialization.MediaListCollectionFilter
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListSort
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus
import io.github.vincentvibe3.anitraklib.anilist.types.MediaType
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat

class AnimeListViewModel():ViewModel() {

    private val _lists = mutableStateMapOf<String, AnimeListCategory>(
        "Watching" to AnimeListCategory("Watching", MediaListStatus.CURRENT, 0),
//        "Completed" to AnimeListCategory("Completed", Categories.COMPLETED, 1)
    )

    val uiState
        get() =  _lists.toSortedMap{ value1, value2 ->
            _lists[value1]!!.index-_lists[value2]!!.index
        }

    private var scoreFormat = ScoreFormat.POINT_10

    fun getList(name:String): AnimeListCategory? {
        return _lists[name]
    }

    fun getScoreFormat(): ScoreFormat {
        return scoreFormat
    }

    fun getShow(id:Int): AnimeCardData? {
       val categories = _lists.flatMap {
           it.value.backingList.toList()
       }
        val shows = categories.associateBy {
            it.id
        }
        return shows[id]
    }

    suspend fun fetchData(){
        val anilist = Clients.anilistClient
        println("getting anilist")
        val currentUser = anilist.getCurrentUser()
        println("got user")
        scoreFormat = currentUser.mediaListOptions.scoreFormat
        val lists = anilist.getMediaList(MediaListCollectionFilter(userId = currentUser.id, type = MediaType.ANIME, sort = listOf(
            MediaListSort.STATUS)))
        var index = 0
        println("got lists")
        lists.lists.forEach{
            val category = AnimeListCategory(it.name, it.status, index)
            println("category created")
            val mappedData = it.entries.mapNotNull { entry ->
                println(entry)
                val title = entry.media?.title?.english
                val score = entry.score
                val progress = entry.progress
                val episodes = entry.media?.episodes
                val image = entry.media?.coverImage?.medium
                val rewatches = entry.repeat
                val notes = entry.notes
                val id = entry.mediaId
                val status = entry.status
                if (title != null &&
                    score != null &&
                    progress != null &&
                    episodes != null &&
                    image != null &&
                    id != null &&
                    rewatches != null &&
                    status != null) {
                    val data = AnimeCardData(
                        title,
                        score,
                        progress,
                        episodes,
                        image,
                        id,
                        status,
                        rewatches,
                        notes
                    )
                    println(data)
                    data
                } else {
                    null
                }
            }
            println(mappedData)
            category.backingList.addAll(mappedData)
            println("added animes")
            _lists[it.name] = category
            index++
        }
    }

    fun moveTo(entry:AnimeCardData, src:String, destination:String){
        _lists[src]?.backingList?.remove(entry)
        _lists[destination]?.backingList?.add(entry)
    }

}