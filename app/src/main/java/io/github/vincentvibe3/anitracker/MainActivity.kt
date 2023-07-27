package io.github.vincentvibe3.anitracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.vincentvibe3.anitracker.anilist.AnimeCardData
import io.github.vincentvibe3.anitracker.anilist.AnimeListViewModel
import io.github.vincentvibe3.anitracker.anilist.Clients
import io.github.vincentvibe3.anitracker.anilist.UpdateAnime
import io.github.vincentvibe3.anitracker.ui.theme.AniTrackerTheme
import io.github.vincentvibe3.anitracker.views.AnimeListLayout
import io.github.vincentvibe3.anitracker.views.MediaEdit
import io.github.vincentvibe3.anitracker.views.SettingsScreen
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            launch {
                Settings.loadAnilistToken(this@MainActivity)
                Clients.setToken()
            }
        }
        if (Settings.anilistToken==""){
            val intent = Intent(this, Setup::class.java)
            this.startActivity(intent)
        }
        val viewModel:AnimeListViewModel by viewModels()
//        println(Settings.anilistToken)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                println("fetching")
                try {
                    viewModel.fetchData()
                    println(viewModel.customLists)
                    println(viewModel.lists)
                } catch (e:Exception){
                    e.printStackTrace()
                    repeat(5) {index ->
                        val animeCardData = AnimeCardData(
                            "$index Genjitsu no Yohane: SUNSHINE in the MIRROR",
                            3f,
                            0,
                            12,
                            "",
                            index,
                            MediaListStatus.CURRENT,
                            0,
                            mutableListOf("Watching"),
                            mutableMapOf("Watching" to false),
                            "",
                            false,
                            false,
                            LocalDate(1,1,1),
                            LocalDate(1,1,1)
//                            ImageResource(ImageResource.ImageType.RESOURCE, R.drawable.yohane.toString()),
                        )
                        println("adding offline")
//                        viewModel.uiState["Watching"]?.backingList?.add(animeCardData)
                    }
                }
            }
        }

        setContent {
            AniTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App(viewModel)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        runBlocking {
            launch {
                Settings.loadAnilistToken(this@MainActivity)
                Clients.setToken()
            }
        }

    }
}

suspend fun setShowComplete(data: AnimeCardData){
    Clients.anilistClient.updateAnime(data.id, status = MediaListStatus.COMPLETED)
}

@Composable
fun App(animeListViewModel:AnimeListViewModel){
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    NavHost(navController = navController, startDestination = "AnimeList") {
        composable("AnimeList") {
            AnimeListLayout(animeListViewModel, animeListViewModel.getScoreFormat(), {
                navController.navigate(it)
            }, {
                navController.navigate("Edit/${it.id}")
            }){
                navController.navigate("Edit/${it.id}")
            }
        }
        composable("Edit/{id}", arguments=listOf(navArgument("id"){ type = NavType.IntType })){
            val id = it.arguments?.getInt("id")
            println("id $id")
            if (id!=null){
                animeListViewModel.getShow(id)?.let { it1 ->
                    MediaEdit(it1, animeListViewModel.customLists, animeListViewModel.getScoreFormat(), {
                        animeListViewModel.moveTo(it1, "Watching", "Deleted")// TODO: Remove temp code
                        navController.navigate("AnimeList")
                    },{ newData ->
                        coroutineScope.launch {
                            UpdateAnime(newData)
                            animeListViewModel.updateData(newData)
                            navController.navigate("AnimeList")
                        }
                    }){
//                        coroutineScope.launch {
//                            Clients.anilistClient.updateAnime(id)
//                        }
                        navController.popBackStack()
                    }
                }
            }
        }
        composable("media/{id}",arguments=listOf(navArgument("id"){ type = NavType.IntType })){
            val id = it.arguments?.getInt("id")
            println("id $id")
            if (id!=null){

            }
        }
        composable("settings"){
            SettingsScreen { navController.popBackStack() }
        }
    }

}


