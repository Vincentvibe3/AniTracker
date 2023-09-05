package io.github.vincentvibe3.anitracker.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.vincentvibe3.anitracker.anilist.AnimeCardData

//@Database(entities = [AnimeCardData::class], version = 1)
abstract class Database:RoomDatabase() {
    abstract fun listDao():ListDAO
}
