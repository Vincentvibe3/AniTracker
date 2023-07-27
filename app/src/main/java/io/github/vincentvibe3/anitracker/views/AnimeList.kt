package io.github.vincentvibe3.anitracker.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.vincentvibe3.anitracker.components.ScoreDialog
import io.github.vincentvibe3.anitracker.components.SectionTitle
import io.github.vincentvibe3.anitracker.components.SwipeAnimeCard
import io.github.vincentvibe3.anitracker.anilist.AnimeCardData
import io.github.vincentvibe3.anitracker.anilist.AnimeListViewModel
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListLayout(animeListViewModel: AnimeListViewModel, scoreFormat: ScoreFormat, navigate: (String)->Unit, onEditRequest: (anime: AnimeCardData)->Unit, onCardPressed: (anime: AnimeCardData) -> Unit){
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet(
                windowInsets = WindowInsets(10.dp, 10.dp, 10.dp, 10.dp)
            ) {
                Column(Modifier.fillMaxHeight(),verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Text(text = "AniTracker", fontWeight = FontWeight.Bold, fontSize = 24.sp, modifier = Modifier.padding(15.dp))
                    Column {
                        NavigationDrawerItem(label = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                                Icon(Icons.Default.List, contentDescription = null)
                                Text(text = "My Anime List (Anilist)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }, selected = true, onClick = { /*TODO*/ })
                        NavigationDrawerItem(label = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                                Icon(Icons.Default.List, contentDescription = null)
                                Text(text = "My Manga List (Anilist)", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }, selected = false, onClick = { /*TODO*/ })
                        NavigationDrawerItem(label = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                                Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                                Text(text = "Top Anime", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }, selected = false, onClick = { /*TODO*/ })
                        NavigationDrawerItem(label = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                                Icon(Icons.Default.DateRange, contentDescription = null)
                                Text(text = "Seasonal Shows", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }, selected = false, onClick = { /*TODO*/ })
                        NavigationDrawerItem(label = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                                Icon(Icons.Default.Settings, contentDescription = null)
                                Text(text = "Settings", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }, selected = false, onClick = {navigate("settings")})
                    }

                }

            }
        }, drawerState=drawerState) {
        Scaffold(
            bottomBar = {
                BottomAppBar(actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            drawerState.open()
                        }
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = {
                        /*TODO*/
                    }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Filter")
                    }
                }, floatingActionButton = {
                    FloatingActionButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add an anime to list")
                    }
                })
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) {
            Column(Modifier.padding(it), horizontalAlignment = Alignment.CenterHorizontally) {
                var searchActive by remember {
                    mutableStateOf(false)
                }
                SearchBar(
                    query = "",
                    onQueryChange = {},
                    onSearch = {},
                    active = searchActive,
                    onActiveChange = { searchActive = it}
                ) {

                }
                AnimeList(PaddingValues(0.dp), animeListViewModel, scoreFormat = scoreFormat,onEditRequest, onCardPressed = onCardPressed){ entry, addedTo, removedFrom ->
                    coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            "Completed ${entry.title}",
                            "Undo",
                            duration = SnackbarDuration.Short)
                        if (result== SnackbarResult.ActionPerformed){
                            animeListViewModel.moveTo(entry, removedFrom, addedTo)
                        } else {
                            println("Add Action Committed")
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimeList(paddingValues: PaddingValues, viewModel: AnimeListViewModel, scoreFormat: ScoreFormat, onEditRequest: (anime: AnimeCardData) -> Unit, onCardPressed:(anime: AnimeCardData)->Unit, onAnimeCompleted:(AnimeCardData, removedFrom:String, addedTo:String) -> Unit){
//    val uiState = viewModel.uiState
    val entries = viewModel.entries
    val lists = viewModel.lists
    var showScoreEdit by remember {
        mutableStateOf(false)
    }
    var dataToEdit: AnimeCardData? by remember {
        mutableStateOf(null as AnimeCardData?)
    }
    if (showScoreEdit) {
        val editEntry = dataToEdit
        if (editEntry!=null) {
            ScoreDialog(scoringMethod = scoreFormat, initialValue = editEntry.score, onChange = {
                editEntry.score = it
            }) {
                showScoreEdit = false
            }
        }
    }
    if (lists.isEmpty()){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator()
        }

    }
    val categories = viewModel.organizedList
    AnimatedVisibility(visible = lists.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                ),
            contentPadding = PaddingValues(15.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            println(categories)
           for (category in categories) {
               val items = category.value
               val list = category.key
               println(list)
               println(items)
               item {
                   SectionTitle(Modifier.animateItemPlacement(), text = list)
                   if (items.isEmpty()) {
                       Row(
                           Modifier
                               .fillMaxWidth()
                               .padding(start = 20.dp, top = 20.dp),
                           horizontalArrangement = Arrangement.Start
                       ) {
                           Text(text = "No anime in $list")
                       }
                   }
               }
               items(category.value, key = { "${it.title}.$list" }) { entry ->
                   SwipeAnimeCard(
                       Modifier.animateItemPlacement(),
                       entry,
                       entry.status,
                       scoreFormat,
                       onEditPressed = onEditRequest,
                       onScorePressed = {
                           dataToEdit = it
                           showScoreEdit = true
                       },
                       onCompletePressed = {
                           viewModel.moveTo(entry, list, "Completed")
                           onAnimeCompleted(entry, list, "Completed")
                       },
                       showComplete = entry.status != MediaListStatus.COMPLETED,
                       onCardPressed = onCardPressed
                   )

               }
           }
        }
    }
}
