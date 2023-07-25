package io.github.vincentvibe3.anitracker.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.vincentvibe3.anitracker.anilist.Clients
import io.github.vincentvibe3.anitracker.components.DatePickerButton
import io.github.vincentvibe3.anitracker.components.ScoreEditor
import io.github.vincentvibe3.anitracker.components.StatusSelector
import io.github.vincentvibe3.anitracker.components.ValueEditor
import io.github.vincentvibe3.anitracker.anilist.AnimeCardData
import io.github.vincentvibe3.anitraklib.anilist.types.FavoriteType
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaEdit(data: AnimeCardData, scoreFormat: ScoreFormat, onBackPressed:()->Unit){
    val scrollState = rememberScrollState()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)
    val scrollDispatcher by remember {
        mutableStateOf(NestedScrollDispatcher())
    }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = { LargeTopAppBar(title = {
            Text(text = data.title)
        }, navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }, scrollBehavior = scrollBehavior)
        },
        bottomBar = {
            BottomAppBar(actions= {
                IconButton(onClick = {
                    coroutineScope.launch {
                        val anilist = Clients.anilistClient
                        println(anilist.favorite(151513, FavoriteType.ANIME))
                    }
                }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Info, contentDescription = "")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Delete, contentDescription = "")
                }
            },floatingActionButton = {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Done, contentDescription = "Add an anime to list")
                }
            }
            )
        }
    ){
        Column(
            Modifier
                .fillMaxWidth()
                .padding(it)
                .nestedScroll(scrollBehavior.nestedScrollConnection, scrollDispatcher)
                .verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column {
                    AsyncImage(model = data.imageUrl, contentDescription = null)
                }
                Text(text = "Status")
                StatusSelector { status ->
                    data.status = when (status) {
                        "Completed" -> MediaListStatus.COMPLETED
                        "Watching" -> MediaListStatus.CURRENT
                        "On Hold" -> MediaListStatus.PAUSED
                        "Repeating" -> MediaListStatus.REPEATING
                        "Plan to Watch" -> MediaListStatus.PLANNING
                        "Dropped" -> MediaListStatus.DROPPED
                        else -> MediaListStatus.CURRENT
                    }
                }
                Text(text = "Score")
                ScoreEditor(data.score, onChange = { newScore ->
                    data.score = newScore
                }, scoringMethod = scoreFormat)
                Text(text = "Episode Progress")
                ValueEditor(0, upperBound = data.totalEpisodes, lowerBound = 0){ progress ->
                    data.progress = progress
                }
                Row {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        Text(text = "Start Date")
                        DatePickerButton()
                    }
                    Column (Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(20.dp)){
                        Text(text = "Start Date")
                        DatePickerButton()
                    }
                }
                Text(text = "Total Rewatches")
                ValueEditor(0, lowerBound = 0){ progress ->
                    data.progress = progress
                }
                Text(text = "Notes")
                TextField(value = "", onValueChange = {}, minLines = 3, modifier = Modifier.fillMaxWidth())
            }

        }
    }
}