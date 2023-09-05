package io.github.vincentvibe3.anitracker.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.vincentvibe3.anitracker.anilist.AnimeCardData

@Dao
interface ListDAO {

//    @Query("SELECT * FROM list")
    fun getAnimeList():List<AnimeCardData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedia(vararg media:AnimeCardData)

    @Update
    fun updateMedia(vararg media:AnimeCardData)

    @Delete
    fun deleteMedia(vararg media:AnimeCardData)
}