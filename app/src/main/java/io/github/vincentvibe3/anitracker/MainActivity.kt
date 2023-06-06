package io.github.vincentvibe3.anitracker

import android.os.Bundle
import android.view.CollapsibleActionView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SearchBar
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Tab
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import io.github.vincentvibe3.anitracker.mal.AnimeCardData
import io.github.vincentvibe3.anitracker.mal.AnimeListViewModel
import io.github.vincentvibe3.anitracker.mal.Categories
import io.github.vincentvibe3.anitracker.mal.ImageResource
import io.github.vincentvibe3.anitracker.ui.theme.AniTrackerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel:AnimeListViewModel by viewModels()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repeat(5) {index ->
                    val animeCardData = AnimeCardData(
                        "$index Mahou Shoujo Madoka★Magica",
                        8.0f,
                        12,
                        12,
                        ImageResource(ImageResource.ImageType.RESOURCE, R.drawable.test_anime_picture_3.toString())
                    )
                    viewModel.uiState["Watching"]?.backingList?.add(animeCardData)
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
}

@Composable
fun App(animeListViewModel:AnimeListViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "AnimeList") {
        composable("AnimeList") {
            AnimeListLayout(animeListViewModel){
                navController.navigate("Edit")
            }
        }
        composable("Edit") {
            EditEntry(){
                navController.popBackStack()
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEntry(onBackPressed:()->Unit){
    Scaffold(
        topBar = { LargeTopAppBar(title = { Text(text = "Edit") }, navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }) }
    ){
        Column(Modifier.padding(it)) {
            Text(text = "Status")
            TextField(value = "", onValueChange = {})
            Text(text = "Score")
            TextField(value = "", onValueChange = {})
            Text(text = "Episode Progress")
            TextField(value = "", onValueChange = {})
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Start Date")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "End Date")
            }
            Text(text = "Total Rewatches")
            TextField(value = "", onValueChange = {})
            Text(text = "Notes")
            TextField(value = "", onValueChange = {})
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimeListLayout(animeListViewModel:AnimeListViewModel, onEditRequest: ()->Unit){
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
                                Text(text = "My Anime List", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }, selected = true, onClick = { /*TODO*/ })
                        NavigationDrawerItem(label = {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                                Icon(Icons.Default.List, contentDescription = null)
                                Text(text = "My Manga List", fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
                        }, selected = false, onClick = { /*TODO*/ })
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
                AnimeList(PaddingValues(0.dp), animeListViewModel, onEditRequest){ entry, addedTo, removedFrom ->
                    coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            "Completed ${entry.title}",
                            "Undo",
                            duration = SnackbarDuration.Short)
                        if (result==SnackbarResult.ActionPerformed){
                            animeListViewModel.moveTo(entry, removedFrom, addedTo)
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AnimeList(paddingValues: PaddingValues, viewModel:AnimeListViewModel, onEditRequest: () -> Unit, onAnimeCompleted:(AnimeCardData, removedFrom:String, addedTo:String) -> Unit){
    val uiState = viewModel.uiState
    var showScoreEdit by remember {
        mutableStateOf(false)
    }
    var dataToEdit:AnimeCardData? by remember {
        mutableStateOf(null as AnimeCardData?)
    }
    if (showScoreEdit) {
        val editEntry = dataToEdit
        if (editEntry!=null) {
            EditScoreScreen(currentScore = editEntry.score, onConfirm = { newScore ->
                editEntry.score = newScore
                showScoreEdit = false
            }) {
                showScoreEdit = false
            }
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
        contentPadding = PaddingValues(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ){
        for (list in uiState){
            item {
                SectionTitle(Modifier.animateItemPlacement() ,text = list.key)
                if (list.value.isEmpty()){
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Text(text = "No anime in ${list.key}")
                    }
                }
            }
            items(list.value.backingList, key = { it.title }){ entry ->
                SwipeAnimeCard(Modifier.animateItemPlacement(), entry, list.value.type,onEditRequest,{
                    dataToEdit = it
                    showScoreEdit = true
                }, onCompletePressed = {
                    viewModel.moveTo(entry, list.key, "Completed")
                    onAnimeCompleted(entry, list.key, "Completed")
                }, showComplete = list.value.type!=Categories.COMPLETED)

            }

        }
    }
}

@Composable
fun SectionTitle(modifier:Modifier, text:String){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp)
            .then(modifier), horizontalArrangement = Arrangement.Center) {
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScoreScreen(
    currentScore:Float,
    onConfirm: (Float) -> Unit,
    onCancel: () -> Unit
){
    var score by remember {
        mutableStateOf(currentScore.toString())
    }
    var validScore by remember {
        mutableStateOf(true)
    }
    AlertDialog(onDismissRequest = onCancel) {
        Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)) {
            Column(
                Modifier
                    .padding(20.dp)
                    .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = "Edit Score",
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                TextField(value=score, onValueChange = {
                    validScore = it.toFloatOrNull()!=null
                    score = it
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onCancel) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = {
                        val floatValue = score.toFloatOrNull()
                        if (floatValue!=null){
                            onConfirm(floatValue)
                        }}, enabled = validScore) {
                        Text(text = "Confirm")
                    }
                }

            }
        }
    }
}

//@Preview(showBackground = true, backgroundColor = 0xF0F0F0)
//@Composable
//fun AnimeCardPreview(){
//    AniTrackerTheme {
//        val image = painterResource(id = R.drawable.test_anime_picture_2)
//        val animeCardData by remember {
//            mutableStateOf(AnimeCardData(
//                "Kidou Senshi Gundam: Suisei no Majo",
//                8.0f,
//                2,
//                12,
//                image
//            ))
//        }
//        Row(
//            Modifier.fillMaxSize(),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            AnimeCard(animeCardData, Categories.WATCHING,{},{})
//        }
//    }
//}