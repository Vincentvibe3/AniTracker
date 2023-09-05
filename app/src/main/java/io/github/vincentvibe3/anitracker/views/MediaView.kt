package io.github.vincentvibe3.anitracker.views

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import io.github.vincentvibe3.anitracker.anilist.MediaViewData
import io.github.vincentvibe3.anitracker.anilist.toPrettyString
import io.github.vincentvibe3.anitracker.components.QuickAccessButton
import io.github.vincentvibe3.anitracker.components.ScoreDisplay
import io.github.vincentvibe3.anitracker.components.getMaxScore
import io.github.vincentvibe3.anitracker.ui.theme.PhosphorIcons
import io.github.vincentvibe3.anitraklib.anilist.serialization.FuzzyDateInt
import io.github.vincentvibe3.anitraklib.anilist.types.MediaFormat
import io.github.vincentvibe3.anitraklib.anilist.types.MediaSource
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class
)

@Composable
fun MediaView(data:MediaViewData, scoringMethod:ScoreFormat){
    val scrollState = rememberScrollState()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        topBar = { TopAppBar(title = { AnimatedVisibility(scrollState.value > 0, enter = fadeIn(), exit = fadeOut()) { Text(text = data.title) } }, navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }, actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(if (data.favourite) PhosphorIcons.heart_fill() else PhosphorIcons.heart(), contentDescription = null)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = PhosphorIcons.share(), contentDescription = null)
            }
        }, scrollBehavior = scrollBehavior) },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Edit")
            }
        }
    ) {
        Column(modifier= Modifier
            .verticalScroll(scrollState)
            .padding(start = 30.dp, end = 30.dp, bottom = 40.dp)
            .padding(it),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp), verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    AsyncImage(
                        model = data.image,
                        contentDescription = null,
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier
//                            .height(205.dp)
                            .fillMaxWidth(1f)
//                            .weight(1f)
                            .aspectRatio(4.67f / 6.47f)
                            .clip(RoundedCornerShape(15.dp))
                    )
                }

                ShowInfoBox(data.score, data.mediaFormat, data.episodeCount, data.episodeLength)
            }

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(text = data.title, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)){
                    items(data.genres){
                        FilterChip(
                            selected = true,
                            onClick = { /*TODO*/ },
                            label = { Text(text = it) },
                            enabled = true
                        )
                    }
                }
//
            }
            AboutShow(data)
            if (data.listInfo!=null) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(text = "Your List", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//                    Surface(color = Color.Gray, shape = RoundedCornerShape(15.dp)) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
//                            modifier = Modifier.padding(15.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                                Button(
                                    onClick = { /*TODO*/ },
                                    shape = RoundedCornerShape(15.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = data.listInfo.status.toPrettyString())
                                }
                                Button(onClick = { /*TODO*/ }, shape = RoundedCornerShape(15.dp)) {
                                    ScoreDisplay(score = data.listInfo.score, maxValue = getMaxScore(scoringMethod), scoreFormat=scoringMethod)
                                }
                                QuickAccessButton(onClick = { /*TODO*/ }, icon = Icons.Default.Edit)
                            }
                            Text(text = "Progress", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                QuickAccessButton(
                                    onClick = { /*TODO*/ },
                                    icon = Icons.Default.KeyboardArrowDown
                                )
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = "${data.listInfo.progress}/${data.listInfo.totalEpisodes}", fontSize = 14.sp)
                                    LinearProgressIndicator(
                                        progress = data.listInfo.progress.toFloat()/data.listInfo.totalEpisodes.toFloat(),
                                        strokeCap = StrokeCap.Round
                                    )
                                }
                                QuickAccessButton(
                                    onClick = { /*TODO*/ },
                                    icon = Icons.Default.KeyboardArrowUp
                                )
                            }
                        }
//                    }
                }
            } else {
                Button(onClick = { /*TODO*/ }, modifier=Modifier.fillMaxWidth()) {
                    Text(text = "Add to list")
                }
            }




            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Synopsis", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                var expanded by remember {
                    mutableStateOf(false)
                }
                val size by remember {
                    derivedStateOf {
                        if (expanded) null else 6
                    }
                }
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(15.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .clickable {
                            expanded = !expanded
                        }
                ) {
                    Column(
                        Modifier.padding(15.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        AnimatedContent(targetState = size, label = "", transitionSpec = {
                            ContentTransform(fadeIn(), fadeOut())
                        }) { size ->
                            if (size != null){
                                Text(
                                    text = data.description,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = size,
                                    fontSize = 14.sp,
//                                modifier = if (size == null) Modifier else Modifier.height(size.dp),
                                )
                            } else {
                                Text(
                                    text = data.description,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 14.sp,
//                                modifier = if (size == null) Modifier else Modifier.height(size.dp),
                                )
                            }
                        }
                        TextButton(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = if (expanded) "See less" else "See more")
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Relations", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "See all")
                    }
                }
                data.relations.forEach { media ->
                    RelationCard(media.name, media.image, media.relationType, media.mediaFormat.toPrettyString() ?: "Unknown")
                }

            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Characters", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "See all")
                    }
                }
                data.characters.forEach { character ->
                    RelationCard(title = character.name, image= character.image, character.characterRole, character.voiceActor)
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Recommendations", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "See all")
                    }
                }
//                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                data.recommendations.forEach { media ->
                    RelationCard(media.name, media.image)
                }
//                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Staff", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(text = "See all")
                    }
                }
//                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                data.staff.forEach { staff ->
                    RelationCard(staff.name, staff.image, staff.staffRole)
                }
//                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Tags", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    data.tags.forEach { tag ->
                        FilterChip(
                            selected = true,
                            onClick = { /*TODO*/ },
                            label = { Text(text = tag) },
                            enabled = true
                        )
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "External Links", fontWeight = FontWeight.Bold, fontSize = 18.sp)
//                    TextButton(onClick = { /*TODO*/ }) {
//                        Text(text = "See All")
//                    }
                }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    data.externalLinks.forEach { link ->
                        OutlinedButton(onClick = { /*TODO*/ }, shape = RoundedCornerShape(15.dp)) {
                            if (!link.image.isNullOrBlank()) {
                                Icon(
                                    rememberAsyncImagePainter(model = link.image),
                                    contentDescription = null,
                                    Modifier.height(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = link.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Streaming", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    data.streamingSources.forEach { link ->
                        OutlinedButton(onClick = { /*TODO*/ }, shape = RoundedCornerShape(15.dp)) {
                            if (!link.image.isNullOrBlank()) {
                                Icon(
                                    rememberAsyncImagePainter(model = link.image),
                                    contentDescription = null,
                                    modifier = Modifier.height(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = link.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RelationCard(
    title:String,
    image:String,
    description:String="",
    secondaryDescription:String=""
){
    Surface(
        color = Color.LightGray,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            Modifier
                .padding(10.dp)
                .wrapContentHeight(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                image,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .height(70.dp)
                    .aspectRatio(4.67f / 6.47f, true)
                    .clip(RoundedCornerShape(15.dp))
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
//                    .background(Color.Gray)
            ) {
                Column {
                    Text(text = title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(
                        text = description,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(text = secondaryDescription, fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AboutShowPreview(){
    val scrollState = rememberScrollState()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    Scaffold(
        topBar = { TopAppBar(title = { AnimatedVisibility(scrollState.value > 0, enter = fadeIn(), exit = fadeOut()) { Text(text = "Horimiya: Piece") } }, navigationIcon = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        }, actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Star, contentDescription = null)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null)
            }
        }, scrollBehavior = scrollBehavior) },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Edit")
            }
        }
    ) {
        Column(modifier= Modifier
            .verticalScroll(scrollState)
            .padding(start = 30.dp, end = 30.dp, bottom = 40.dp)
            .padding(it),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            AboutShow(
                data = MediaViewData(
                    mediaId = 1,
                    title = "Horimiya: Piece",
                    score = 91,
                    image = "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg",
                    scoreFormat = ScoreFormat.POINT_10,
                    mediaFormat = MediaFormat.TV,
                    episodeCount = 26,
                    episodeLength = 24,
                    studioNames = listOf("A1-Pictures", "Cloverworks", "Trigger"),
                    genres = listOf("Romance", "Slice of Life"),
                    sourceFormat = MediaSource.MANGA,
                    season = "Summer 2023",
                    startDate = FuzzyDateInt(2023, 7, 1),
                    endDate = FuzzyDateInt(2023, 9, 23),
                    popularity = 60820,
                    description = "Alchemy Incident",
                    relations = listOf(
                        MediaViewData.Relation(
                            "Horimiya",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg",
                            "parent",
                            MediaFormat.MANGA
                        ),
                        MediaViewData.Relation(
                            "Horimiya",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg",
                            "prequel",
                            MediaFormat.TV
                        )
                    ),
                    recommendations = listOf(
                        MediaViewData.Recommendation(
                            "Name",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg"
                        ),
                        MediaViewData.Recommendation(
                            "Name2",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg"
                        )
                    ),
                    characters = listOf(
                        MediaViewData.MediaCharacterEntry(
                            "Hori",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg",
                            "main",
                            "vaName"
                        ),
                        MediaViewData.MediaCharacterEntry(
                            "Miyamura",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg",
                            "main",
                            "vaName"
                        )
                    ),
                    externalLinks = listOf(
                        MediaViewData.ExternalResource(
                            "Twitter",
                            "link",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg"
                        ),
                        MediaViewData.ExternalResource(
                            "Youtube",
                            "link",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg"
                        )
                    ),
                    staff = listOf(
                        MediaViewData.MediaStaff(
                            "Director",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg",
                            "director"
                        ),
                        MediaViewData.MediaStaff(
                            "Key Animator",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg",
                            "Key Animation"
                        )
                    ),
                    tags = listOf(
                        "Comedy",
                        "Romance"
                    ),
                    streamingSources = listOf(
                        MediaViewData.ExternalResource(
                            "Crunchyroll",
                            "link",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg"
                        ),
                        MediaViewData.ExternalResource(
                            "HIDIVE",
                            "link",
                            "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx163132-C220CO5UrTxY.jpg"
                        )
                    ),
                    favourite = true
                )
            )
        }
    }
}

@Composable
fun AboutShow(data: MediaViewData){
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.weight(0.33f, true)) {
                Text(text = "Studio${if (data.studioNames.size>1) 's'.toString() else String()}", fontSize = 12.sp)
                Text(text = data.studioNames.joinToString(",\n"), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Column(Modifier.weight(0.33f, true)) {
                Text(text = "Source", fontSize = 12.sp)
                Text(
                    text = data.sourceFormat.toPrettyString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(Modifier.weight(0.33f, true)) {
                Text(text = "Popularity", fontSize = 12.sp)
                Text(
                    text = data.popularity.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.weight(0.33f, true)) {
                Text(text = "Season", fontSize = 12.sp)
                Text(text = data.season, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Column(Modifier.weight(0.33f, true)) {
                Text(text = "Start Date", fontSize = 12.sp)
                Text(text = "Jul 1, 2023", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Column(Modifier.weight(0.33f, true)) {
                Text(text = "End Date", fontSize = 12.sp)
                Text(text = "Sep 23, 2023", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RowScope.ShowInfoBox(
    score: Int,
    mediaFormat: MediaFormat,
    episodeCount: Int,
    episodeLength: Int,
) {
    Surface(
        color = Color.Gray,
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
//            .height(IntrinsicSize.Min)
//            .height(205.dp)
//            .fillMaxHeight()
            .wrapContentHeight()
//            .aspectRatio(1f, true)
//            .fillMaxWidth()
            .weight(1f)
//            .aspectRatio(1f, true)
    ) {
        Column(
            Modifier.padding(20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ){
                Column {
                    Text(text = "Score", fontSize = 12.sp)
                    Text(text = "${score.toFloat() / 10}", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = "Format", fontSize = 12.sp)
                    Text(text = mediaFormat.toPrettyString() ?: "Unknown", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(text = "Episodes", fontSize = 12.sp)
                    Text(text = episodeCount.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text(
                        text = "Episode length",
                        fontSize = 12.sp,
                    )
                    Text(
                        text = "$episodeLength min",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }

    }
}