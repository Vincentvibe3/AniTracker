package io.github.vincentvibe3.anitracker.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.github.vincentvibe3.anitracker.anilist.Clients
import io.github.vincentvibe3.anitracker.anilist.AnimeCardData
import io.github.vincentvibe3.anitracker.anilist.CategoryColors
import io.github.vincentvibe3.anitracker.ui.theme.AniTrackerTheme
import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlin.math.abs
import kotlin.math.roundToInt

@Composable
fun AnimeCard(
    data:AnimeCardData,
    entryType: MediaListStatus,
    scoreFormat: ScoreFormat,
    onCardPressed: (anime: AnimeCardData) -> Unit,
    onEditPressed: (anime: AnimeCardData) -> Unit,
    onScorePressed: (AnimeCardData) -> Unit,
    showComplete:Boolean=true,
    onCompletePressed: () -> Unit = {}
) {
    Box(
        Modifier
            .wrapContentHeight()
            .height(IntrinsicSize.Min)
    ){
        val clickInteractionSource = remember{MutableInteractionSource()}
        Card(
            modifier = Modifier
                .wrapContentHeight()
                .height(IntrinsicSize.Min)
                .clip(RoundedCornerShape(20.dp))
                .clickable(clickInteractionSource, LocalIndication.current) {
                    onCardPressed(data)
                },
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(Color(0xFFFFFFFF)),
        ) {
            Row(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Column {
                    AsyncImage(
                        model = data.imageUrl,
                        contentDescription = null,
                        contentScale=ContentScale.FillHeight,
                        modifier = Modifier
                            .aspectRatio(4.67f / 6.47f, true)
                            .heightIn(75.dp, 75.dp)
                            .clip(RoundedCornerShape(15.dp))
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = data.title,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Progress: ${data.progress}/${data.totalEpisodes}",
                            fontSize = 12.sp
                        )
                    }
                    Row(
                        Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val buttonColor = CategoryColors.colors[entryType] ?: 0xFF7E7E7E
                        Button(
                            onClick = { onScorePressed(data) },
                            colors = ButtonDefaults.buttonColors(Color(buttonColor)),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            val maxValue = getMaxScore(scoreFormat)
                            ScoreDisplay(score = data.score, maxValue = maxValue, scoreFormat = scoreFormat)
                        }
                        Row {
                            if (showComplete) {
                                QuickAccessButton(
                                    onClick = onCompletePressed,
                                    icon = Icons.Outlined.Done
                                )
                            }
                            QuickAccessButton(
                                onClick = {onEditPressed(data)},
                                icon = Icons.Outlined.Edit
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Composable
fun anchors(data: AnimeCardData, sizePx:Float): Map<Float, Int> {
    val map = mutableMapOf(0f to 0)
    if (data.progress>0){
        map[sizePx] = 1
    }
    if (data.progress<data.totalEpisodes){
        map[-sizePx] = -1
    }
    return map.toMap()
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeAnimeCard(
    modifier: Modifier=Modifier,
    data:AnimeCardData,
    entryType: MediaListStatus,
    scoreFormat: ScoreFormat,
    onCardPressed: (anime: AnimeCardData) -> Unit,
    onEditPressed: (anime: AnimeCardData) -> Unit,
    onScorePressed: (AnimeCardData) -> Unit,
    showComplete:Boolean=true,
    onCompletePressed: () -> Unit = {}
){
    val coroutineScope = rememberCoroutineScope()
    val swipeableState = rememberSwipeableState(0, animationSpec = tween(100))
    var swipeEnabled by remember { mutableStateOf(true) }
    val squareSize = 48.dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    var anchors by remember {
        mutableStateOf(anchors(data = data, sizePx = sizePx))
    }
    val bgYScale by remember {
        derivedStateOf{
            abs((swipeableState.offset.value / sizePx)).coerceIn(0.4f, 1f)
        }
    }
    val bgOffset by animateIntAsState(
        -swipeableState.direction.toInt() * sizePx.toInt() - swipeableState.offset.value.roundToInt(), label = ""
    )
    val scaleYAnimation by animateFloatAsState(targetValue = bgYScale, label = "")
    var lastBgColor by remember {
        mutableStateOf(Color.White)
    }
    val bgColor by remember {
        derivedStateOf {
            if (swipeableState.targetValue==1){
                lastBgColor= Color(0xFFB57E7E)
            } else if (swipeableState.targetValue==-1) {
                lastBgColor= Color(0xFF7EB583)
            }
            lastBgColor
        }
    }
    val bgArrangement by remember {
        derivedStateOf {
            if (swipeableState.direction == 1f){
                Arrangement.Start
            } else {
                Arrangement.End
            }
        }
    }
    val swipeIcon by remember {
        derivedStateOf {
            if (swipeableState.direction == 1f){
                Icons.Filled.Clear
            } else {
                Icons.Filled.Add
            }
        }
    }
    Box(
        Modifier
            .wrapContentHeight()
            .height(IntrinsicSize.Min)
            .then(modifier)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(scaleYAnimation)
                    .offset { IntOffset(bgOffset, 0) },
                colors = CardDefaults.cardColors(bgColor),
                shape = RoundedCornerShape(20.dp),
            ) {
                Row(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = bgArrangement,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(16.dp),
                        imageVector = swipeIcon,
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            }
        }
        Box(
            Modifier
                .offset {
                    IntOffset(swipeableState.offset.value.roundToInt(), 0)
                }
                .swipeable(
                    state = swipeableState,
                    anchors = anchors,
                    enabled = swipeEnabled,
                    orientation = Orientation.Horizontal,
                    thresholds = { _, _ -> FractionalThreshold(0.8f) }
                )
        ) {
            if (swipeableState.currentValue == 1 && !swipeableState.isAnimationRunning) {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        data.progress--
                        val anilist = Clients.anilistClient
                        anilist.updateProgress(data.id, data.progress)
                        swipeEnabled = false
                        swipeableState.animateTo(0)
                        anchors = anchors(data, sizePx)
                        swipeEnabled = true
                        println(anchors)
                    }
                }
            } else if (swipeableState.currentValue == -1 && !swipeableState.isAnimationRunning) {
                LaunchedEffect(Unit) {
                    coroutineScope.launch {
                        data.progress++
                        val anilist = Clients.anilistClient
                        anilist.updateProgress(data.id, data.progress)
                        swipeEnabled = false
                        swipeableState.animateTo(0)
                        anchors = anchors(data, sizePx)
                        swipeEnabled = true
                    }
                }
            }
            AnimeCard(
                data,
                entryType = entryType,
                scoreFormat = scoreFormat,
                onEditPressed = onEditPressed,
                onCardPressed = onCardPressed,
                onScorePressed = onScorePressed,
                onCompletePressed = onCompletePressed,
                showComplete = showComplete
            )
        }
    }
}

@Preview
@Composable
fun AnimeCardPreview(){
    AniTrackerTheme {
        val animeCardData by remember {
            mutableStateOf(AnimeCardData(
                "Genjitsu no Yohane: SUNSHINE in the MIRROR",
                3f,
                0,
                12,
                "https://s4.anilist.co/file/anilistcdn/media/anime/cover/large/bx151513-0tGiS0NC3adv.jpg",
                1,
                MediaListStatus.CURRENT,
                0,
//                mutableListOf("Watching"),
                mutableMapOf(),
                "",
                false,
                false,
                LocalDate(0,0,0),
                LocalDate(0,0,0)
            ))
        }
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SwipeAnimeCard(
                data=animeCardData,
                entryType = MediaListStatus.CURRENT,
                scoreFormat = ScoreFormat.POINT_10,
                onCardPressed = {},
                onCompletePressed = {},
                onEditPressed = {},
                onScorePressed = {}
            )
        }
    }
}