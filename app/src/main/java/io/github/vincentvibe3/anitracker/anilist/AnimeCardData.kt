package io.github.vincentvibe3.anitracker.anilist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat
import kotlinx.datetime.LocalDate

//@Entity(tableName = "list")
data class AnimeCardData(
    @PrimaryKey val title: String,
    var score:Float,
    var progress:Int,
    val totalEpisodes:Int,
    val imageUrl:String,
    val id:Int,
    var status: MediaListStatus,
    var rewatches:Int,
    var customLists:Map<String, Boolean>,
    var notes:String?,
    var private:Boolean,
    var favourite:Boolean,
    var startedAt:LocalDate?,
    var completedAt:LocalDate?
){
    fun deepCopy():AnimeCardData{
        val startedAtTemp = startedAt
        val startedAtCopy = if (startedAtTemp==null){
            null
        } else {
            LocalDate(startedAtTemp.year, startedAtTemp.monthNumber, startedAtTemp.dayOfMonth)
        }
        val completedAtTemp = startedAt
        val completedAtCopy = if (completedAtTemp==null){
            null
        } else {
            LocalDate(completedAtTemp.year, completedAtTemp.monthNumber, completedAtTemp.dayOfMonth)
        }
        return this.copy(
//            lists = mutableListOf<String>().apply { addAll(lists) },
            customLists = mutableMapOf<String, Boolean>().apply { customLists.forEach { (k, v) -> this[k] = v  } },
            startedAt = startedAtCopy,
            completedAt = completedAtCopy
        )
    }
}