package io.github.vincentvibe3.anitracker

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object Settings {

    var anilistToken = ""

    suspend fun loadAnilistToken(context:Context){
        anilistToken = context.dataStore.data.map {
            it[stringPreferencesKey("anilistToken")] ?: ""
        }.first()
    }

}