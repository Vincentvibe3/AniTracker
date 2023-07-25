package io.github.vincentvibe3.anitracker

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AnilistOAuthProcessor : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.data
        if (uri != null) {
            val params = extractParams(uri)
            runBlocking {
                launch {
                    this@AnilistOAuthProcessor.dataStore.edit {
                        it[stringPreferencesKey("anilistToken")] = params["access_token"] ?: ""
                        it[intPreferencesKey("anilistExpiry")] = params["expires_in"]?.toInt() ?: -1
                    }
                    println("saved")
                }
            }
        }
        this.finish()
    }

    private fun extractParams(uri: Uri): HashMap<String, String> {
        val params = hashMapOf<String, String>()
        val entries = uri.fragment?.split("&")
        if (entries != null) {
            for (param in entries){
                val (key, value) = param.split("=")
                params[key] = value
            }
        }
        return params
    }
}