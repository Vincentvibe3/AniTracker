package io.github.vincentvibe3.anitracker.anilist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.ViewModel
import io.github.vincentvibe3.anitraklib.anilist.serialization.FuzzyDateInt
import io.github.vincentvibe3.anitraklib.anilist.serialization.MediaListCollectionFilter
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListSort
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus
import io.github.vincentvibe3.anitraklib.anilist.types.MediaType
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive
import java.util.Locale

class AnimeListViewModel():ViewModel() {

//    private val _lists = mutableStateMapOf<String, AnimeListCategory>(
//        "Watching" to AnimeListCategory("Watching", MediaListStatus.CURRENT, 0),
//        "Completed" to AnimeListCategory("Completed", Categories.COMPLETED, 1)
//    )

//    val uiState
//        get() =  _lists.toSortedMap{ value1, value2 ->
//            _lists[value1]!!.index-_lists[value2]!!.index
//        }

    private val _entries = mutableStateListOf<AnimeCardData>()
    private val _lists = mutableStateListOf<String>()
    private val _customLists = mutableStateListOf<String>()

    val entries
        get() = _entries.toList()
    val lists
        get() = _lists.toList()

    val customLists
        get() = _customLists.toList()

    val organizedList
        get() = _lists.associateWith { listName -> _entries.filter {
//            it.lists.contains(listName)||

                    it.customLists.getOrDefault(listName,false)
        } }

    private var scoreFormat = ScoreFormat.POINT_10

    fun getScoreFormat(): ScoreFormat {
        return scoreFormat
    }

    fun getShow(id:Int): AnimeCardData? {
        return entries.firstOrNull{ it.id == id }
//       val categories = _lists.flatMap {
//           it.value.backingList.toList()
//       }
//        val shows = categories.associateBy {
//            it.id
//        }
//        return shows[id]
    }

    fun updateData(data: AnimeCardData){
        val removed = _entries.removeIf {
            it.id==data.id
        }
        println(removed)
        println(data)
        _entries.add(data)
    }

    fun removeEntry(data:AnimeCardData){
        _entries.remove(data)
    }

    fun addEntry(data: AnimeCardData){
        _entries.add(data)
    }

    fun offlineFetch(){
        _lists.addAll(listOf("Watching", "Completed"))
        repeat(6){ index ->
            val animeCardData = AnimeCardData(
                "$index Genjitsu no Yohane: SUNSHINE in the MIRROR",
                3f,
                0,
                12,
                "",
                index,
                MediaListStatus.CURRENT,
                0,
//                mutableListOf("Watching"),
                mutableMapOf("Watching" to false),
                "",
                false,
                false,
                LocalDate(1,1,1),
                LocalDate(1,1,1)
//                            ImageResource(ImageResource.ImageType.RESOURCE, R.drawable.yohane.toString()),
            )
            _entries.add(
                animeCardData
            )
        }
    }

    suspend fun fetchData(){
        val anilist = Clients.anilistClient
        println("getting anilist")
        val currentUser = anilist.getCurrentUser()
        println("got user")
        scoreFormat = currentUser.mediaListOptions.scoreFormat
        val customLists = currentUser.mediaListOptions.animeList.customLists
        _customLists.addAll(customLists)
        println("customLists ${_customLists.toList()}")
        val lists = anilist.getMediaList(MediaListCollectionFilter(userId = currentUser.id, type = MediaType.ANIME, sort = listOf(
            MediaListSort.STATUS)))
        var index = 0
        println("got lists")
        lists.lists.forEach{
            if (!it.isCustomList){
                _lists.add(it.name)
            }
//            val category = AnimeListCategory(it.name, it.status, index)
//            println("category created")
            val mappedData = it.entries.mapNotNull { entry ->
//                println(entry)
                val title = entry.media?.title?.english
                val score = entry.score
                val progress = entry.progress
                val episodes = entry.media?.episodes
                val image = entry.media?.coverImage?.medium
                val rewatches = entry.repeat
                val notes = entry.notes
                val id = entry.mediaId
                val status = entry.status
                val private = entry.private
                val favourite = entry.media?.isFavourite
                val startedAt = entry.startedAt
                val completedAt = entry.completedAt
                val entryCustomLists = entry.customLists?.toMap()?.mapValues { pair ->
                    pair.value.jsonPrimitive.boolean
                }
                if (title != null &&
                    score != null &&
                    progress != null &&
                    episodes != null &&
                    image != null &&
                    id != null &&
                    rewatches != null &&
                    status != null &&
                    private != null &&
                    favourite != null &&
                    startedAt != null &&
                    completedAt != null &&
                    entryCustomLists != null) {
                    val existingEntry = _entries.firstOrNull { filterEntry -> filterEntry.title == title }
                    if (existingEntry==null){
                        val data = AnimeCardData(
                            title,
                            score,
                            progress,
                            episodes,
                            image,
                            id,
                            status,
                            rewatches,
                            entryCustomLists,
                            notes,
                            private,
                            favourite,
                            startedAt.toLocalDate(),
                            completedAt.toLocalDate()
                        )
//                        println(data)
                        data
                    } else {
//                        existingEntry.lists.add(it.name)
                        null
                    }
                } else {
                    null
                }
            }
//            println(mappedData)
            _entries.addAll(mappedData)
//            category.backingList.addAll(mappedData)
            println("added animes")
//            _lists[it.name] = category
            index++
        }
        _lists.addAll(_customLists)
    }

    fun moveTo(entry:AnimeCardData, src:String, destination:String){
//        _lists[src]?.backingList?.remove(entry)
//        _lists[destination]?.backingList?.add(entry)
    }

}