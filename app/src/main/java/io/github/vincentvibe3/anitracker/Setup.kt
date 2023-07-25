package io.github.vincentvibe3.anitracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import io.github.vincentvibe3.anitracker.anilist.Clients
import io.github.vincentvibe3.anitracker.ui.theme.AniTrackerTheme
import io.github.vincentvibe3.anitracker.views.SetupScreen
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Setup : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AniTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupScreen()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            launch {
                Settings.loadAnilistToken(this@Setup)
                Clients.setToken()
//
            }
        }
        if (Settings.anilistToken!=""){
            this@Setup.finish()
        }
    }
}