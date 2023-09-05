package io.github.vincentvibe3.anitracker.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.github.vincentvibe3.anitracker.anilist.Clients
import io.github.vincentvibe3.anitracker.components.DatePickerButton
import io.github.vincentvibe3.anitracker.components.ScoreEditor
import io.github.vincentvibe3.anitracker.components.StatusSelector
import io.github.vincentvibe3.anitracker.components.ValueEditor
import io.github.vincentvibe3.anitracker.anilist.AnimeCardData
import io.github.vincentvibe3.anitracker.components.CustomListSelector
import io.github.vincentvibe3.anitracker.components.DialogBox
import io.github.vincentvibe3.anitracker.components.QuickAccessButton
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MediaEdit(
    initialData: AnimeCardData, scoreFormat: ScoreFormat,
    onDeleted:()->Unit, onSave:(AnimeCardData)->Unit ,onBackPressed:()->Unit){
    val scrollState = rememberScrollState()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val scrollDispatcher by remember {
        mutableStateOf(NestedScrollDispatcher())
    }
    var deletionDialogOpen by remember {
        mutableStateOf(false)
    }
    val data by remember {
        println("copy ${initialData.copy()}")
        mutableStateOf(initialData.copy())
    }
    val customListMap = remember {
        val map = mutableStateMapOf<String, Boolean>()
        data.customLists.forEach {
            map[it.key] = it.value
        }
        map
    }
    var notes by remember {
        mutableStateOf(data.notes?: "")
    }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = { TopAppBar(title = {
            Text(text = "Edit")
        }, navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }, scrollBehavior = scrollBehavior)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                data.customLists = customListMap
                println(data)
                onSave(data)
            }) {
                Icon(Icons.Filled.Done, contentDescription = "Add an anime to list")
            }
        }
    ){
        Column(
            Modifier
                .fillMaxWidth()
                .padding(it)
                .consumeWindowInsets(it)
                .nestedScroll(scrollBehavior.nestedScrollConnection, scrollDispatcher)
                .verticalScroll(scrollState)
                .padding(start = 20.dp, end = 20.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AsyncImage(model = data.imageUrl, contentDescription = null)
            Text(text = data.title, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                QuickAccessButton(onClick = {
                    coroutineScope.launch {
                        val anilist = Clients.anilistClient
//                            println(anilist.favorite(151513, FavoriteType.ANIME))
                        data.favourite = !data.favourite
                    } }, icon = { Icon(Icons.Default.FavoriteBorder, contentDescription = null) }, modifier = Modifier.size(48.dp))
                QuickAccessButton(onClick = {
                    coroutineScope.launch {
                        val anilist = Clients.anilistClient
//                            println(anilist.favorite(151513, FavoriteType.ANIME))
                        data.private = !data.private
                    } }, icon = { Icon(Icons.Default.Info,contentDescription = null) }, modifier = Modifier.size(48.dp))
                QuickAccessButton(onClick = {
                    deletionDialogOpen=true
                }, icon = { Icon(Icons.Default.Delete,contentDescription = null) }, modifier = Modifier.size(48.dp), color = Color(0xFFf23a3a))
            }
            if(deletionDialogOpen){
                DialogBox(
                    title = "Delete",
                    onDismissRequest = { deletionDialogOpen=false },
                    onConfirm = {
                        //Delete here
                        deletionDialogOpen = false
                        onDeleted()
                    },
                    isValid = true,
                    onConfirmColor = ButtonDefaults.buttonColors(containerColor = Color(0xFFf23a3a))
                ) {
                    Text(text = "Are you sure you want to remove ${data.title} from your list?")
                }
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
            Text(text = "Total Rewatches")
            ValueEditor(0, lowerBound = 0){ rewatches ->
                data.rewatches = rewatches
            }
            Row {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Text(text = "Start Date")
                    DatePickerButton(data.startedAt){ newDate ->
                        data.startedAt = newDate
                    }
                }
                Column (Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(20.dp)){
                    Text(text = "End Date")
                    DatePickerButton(data.completedAt){ newDate ->
                        data.completedAt = newDate
                    }
                }
            }
            Text(text = "Custom Lists")
            CustomListSelector(customListMap){ listName, state ->
                customListMap[listName] = state
                println(state)
                println("updated customLists")
            }
            Text(text = "Notes")
            TextField(value = notes, onValueChange = { newValue ->
                notes = newValue
                if (newValue!=""){
                    data.notes = newValue
                } else {
                    data.notes = null
                }
            }, minLines = 3, modifier = Modifier.fillMaxWidth())
//            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}