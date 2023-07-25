package io.github.vincentvibe3.anitracker.anilist

import io.github.vincentvibe3.anitracker.Settings
import io.github.vincentvibe3.anitraklib.anilist.Anilist

object Clients {

    val anilistClient = Anilist()

    fun setToken(){
        anilistClient.token = Settings.anilistToken
    }

}