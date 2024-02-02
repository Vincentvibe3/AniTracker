package io.github.vincentvibe3.anitracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.vincentvibe3.anitracker.anilist.AnimeCardData
import io.github.vincentvibe3.anitracker.anilist.AnimeListViewModel
import io.github.vincentvibe3.anitracker.anilist.Clients
import io.github.vincentvibe3.anitracker.anilist.MediaViewData
import io.github.vincentvibe3.anitracker.anilist.MediaViewModel
import io.github.vincentvibe3.anitracker.anilist.toMediaViewData
import io.github.vincentvibe3.anitracker.ui.theme.AniTrackerTheme
import io.github.vincentvibe3.anitracker.views.AnimeListLayout
import io.github.vincentvibe3.anitracker.views.MediaEdit
import io.github.vincentvibe3.anitracker.views.MediaView
import io.github.vincentvibe3.anitracker.views.SettingsScreen
import io.github.vincentvibe3.anitraklib.anilist.serialization.FuzzyDateInt
import io.github.vincentvibe3.anitraklib.anilist.types.MediaFormat
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus
import io.github.vincentvibe3.anitraklib.anilist.types.MediaSource
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
//        val db = Room.databaseBuilder(
//            applicationContext,
//            Database::class.java, "cachedb"
//        ).build()

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
                    viewModel.offlineFetch()
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
            Column {
                Button(onClick = { navController.navigate("media/163132") }) {
                    Text("Go to Media")
                }
                AnimeListLayout(animeListViewModel, animeListViewModel.getScoreFormat(), {
                    navController.navigate(it)
                }, {
                    navController.navigate("Edit/${it.id}")
                }){
                    navController.navigate("media/${it.id}")
                }
            }

        }
        composable("Edit/{id}", arguments=listOf(navArgument("id"){ type = NavType.IntType })){
            val id = it.arguments?.getInt("id")
            println("id $id")
            if (id!=null){
                animeListViewModel.getShow(id)?.let { it1 ->
                    MediaEdit(it1, animeListViewModel.getScoreFormat(), {
                        animeListViewModel.moveTo(it1, "Watching", "Deleted")// TODO: Remove temp code
                        navController.navigate("AnimeList")
                    },{ newData ->
                        coroutineScope.launch {
//                            UpdateAnime(newData)
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
                var data by remember {
                    mutableStateOf(
                        MediaViewData(
                            mediaId = id,
                            title = "Horimiya: Piece",
                            score = 92,
                            image = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg",
                            scoreFormat = ScoreFormat.POINT_10,
                            mediaFormat = MediaFormat.TV,
                            episodeCount = 26,
                            episodeLength = 24,
                            studioNames = listOf("Trigger","A1-Pictures", "Cloverworks"),
                            genres = listOf("Romance", "Slice of Life"),
                            sourceFormat = MediaSource.MANGA,
                            season = "Summer 2023",
                            startDate = FuzzyDateInt(2023, 7, 1),
                            endDate = FuzzyDateInt(2023, 9 , 23),
                            popularity = 60820,
                            description = "Alchemy Incident",
                            relations = listOf(
                                MediaViewData.Relation("Horimiya", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg", "parent", MediaFormat.MANGA),
                                MediaViewData.Relation("Horimiya", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg", "prequel", MediaFormat.TV)),
                            recommendations = listOf(
                                MediaViewData.Recommendation("Name", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg"),
                                MediaViewData.Recommendation("Name2", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg")
                            ),
                            characters = listOf(
                                MediaViewData.MediaCharacterEntry("Hori", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg", "main", "vaName") ,
                                MediaViewData.MediaCharacterEntry("Miyamura", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg", "main", "vaName")
                            ),
                            externalLinks = listOf(
                                MediaViewData.ExternalResource("Twitter", "link", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg"),
                                MediaViewData.ExternalResource("Youtube", "link", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg")
                            ),
                            staff = listOf(
                                MediaViewData.MediaStaff("Director", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg", "director"),
                                MediaViewData.MediaStaff("Key Animator", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg", "Key Animation")
                            ),
                            tags = listOf(
                                "Comedy",
                                "Romance"
                            ),
                            streamingSources = listOf(
                                MediaViewData.ExternalResource("Crunchyroll", "link", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg"),
                                MediaViewData.ExternalResource("HIDIVE", "link", "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg")
                            ),
                            favourite = true
                        )
                    )
                }

                LaunchedEffect(key1 = id){
                    if (id!=0){
                        println("id is $id")
                        val media = Clients.anilistClient.getMedia(id)
                        println(media)
                        val newData = media?.toMediaViewData(ScoreFormat.POINT_10)
                        println(newData?.sourceFormat)
                        if (newData!=null){
                            data=newData
                        }
                    }


                }
                MediaView(data, ScoreFormat.POINT_10)
            }
        }
        composable("settings"){
            SettingsScreen { navController.popBackStack() }
        }
    }

}


