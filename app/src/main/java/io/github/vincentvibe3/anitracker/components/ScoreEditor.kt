package io.github.vincentvibe3.anitracker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.vincentvibe3.anitracker.ui.theme.PhosphorIcons
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat
import kotlin.math.roundToInt

@Composable
@Preview
private fun ScoreEditorPreview(){
    ScoreEditor(6f, onChange = {}, ScoreFormat.POINT_3)
}

@Composable
fun ScoreEditor(
    initialValue: Float,
    onChange:(Float)->Unit,
    scoringMethod: ScoreFormat
){
    var value by remember {
        mutableStateOf(convertScore(initialValue, scoringMethod))
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    val maxValue by remember {
        derivedStateOf {
            getMaxScore(scoringMethod)
        }
    }
    Row(
        Modifier
            .wrapContentHeight()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        QuickAccessButton({
            if (scoringMethod==ScoreFormat.POINT_100){
                value-=10
            } else {
                value--
            }
            onChange(value)
        }, Icons.Outlined.KeyboardArrowDown, enabled = if(scoringMethod==ScoreFormat.POINT_100){
            value>9
        } else {
            value>0
        })
        Button(onClick = { showDialog = !showDialog }, modifier = Modifier
            .weight(1f)
            .fillMaxHeight(),
//            .then(borderModifier)
//            .background(Color(0xFFD4D4D4), RoundedCornerShape(10.dp))
            shape = RoundedCornerShape(10.dp)
            ) {
//            Text(displayText)
            ScoreDisplay(score = value, maxValue = maxValue, scoreFormat = scoringMethod)
        }
        QuickAccessButton({
            if (scoringMethod==ScoreFormat.POINT_100){
                value+=10
            } else {
                value++
            }
            onChange(value)
        }, Icons.Outlined.KeyboardArrowUp, enabled = if(scoringMethod==ScoreFormat.POINT_100){
            value<=maxValue-10
        } else {
            value<maxValue
        })
        if (showDialog){
            ScoreDialog(scoringMethod = scoringMethod, initialValue = value, onChange = {
                value = it
                onChange(it)
            }) {
                showDialog=false
            }
        }
    }
}

@Composable
fun ScoreDialog(scoringMethod: ScoreFormat, initialValue:Float, onChange:(Float)->Unit, onDismissRequest: () -> Unit){
    var value by remember {
        mutableStateOf(convertScore(initialValue, scoringMethod))
    }
    when(scoringMethod){
        ScoreFormat.POINT_3 -> DialogPoint3(value.toInt(), {
            onDismissRequest()
        }, {
            onDismissRequest()
            value = it
            onChange(value)
        })
        ScoreFormat.POINT_5 -> DialogPoint5(value.toInt(), {
            onDismissRequest()
        }, {
            onDismissRequest()
            value = it
            onChange(value)
        })
        ScoreFormat.POINT_10 ->  DialogPoint10(value.toInt(), {
            onDismissRequest()
        }, {
            onDismissRequest()
            value = it
            onChange(value)
        })
        ScoreFormat.POINT_10_DECIMAL ->  DialogPoint10Decimal(value, {
            onDismissRequest()
        }, {
            onDismissRequest()
            value = it
            onChange(value)
        })
        ScoreFormat.POINT_100 ->  DialogPoint100(value.toInt(), {
            onDismissRequest()
        }, {
            onDismissRequest()
            value = it
            onChange(value)
        })
    }
}

@Composable
fun ScoreDisplay(score:Float, maxValue:Float, scoreFormat: ScoreFormat, size:TextUnit?=null){
    val sizedText:@Composable (String) -> Unit = if(size==null) {
        @Composable { value: String -> Text(text = value) }
    } else {
        @Composable { value: String -> Text(text = value, fontSize = size)}
    }
    val sp2Dp = with(LocalDensity.current) { size?.toDp() ?: 16.sp.toDp() }
    if (score.toInt()==0){
        Text(text = "No Score")
    } else {
        when (scoreFormat){
            ScoreFormat.POINT_3 -> {
                val icon = if (score==1f){
                    PhosphorIcons.smiley_sad()
                } else if (score==2f){
                    PhosphorIcons.smiley_meh()
                } else if (score==3f){
                    PhosphorIcons.smiley()
                } else {
                    Icons.Filled.Clear
                }
                Icon(
                    imageVector = icon,
                    contentDescription = "",
                    modifier = Modifier.size(sp2Dp)
                )
            }
            ScoreFormat.POINT_5 -> {
//                repeat(score.toInt()){
                    sizedText("${score.toInt()}")
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "",
                        modifier = Modifier.size(sp2Dp)
                    )
//                }
            }
            else -> {
                val displayText = if (scoreFormat==ScoreFormat.POINT_10_DECIMAL){
                    "$score/$maxValue"
                } else {
                    "${score.toInt()}/${maxValue.toInt()}"
                }
                sizedText(displayText)
            }
        }
    }

}

fun getMaxScore(scoreFormat:ScoreFormat): Float{
    return when (scoreFormat){
        ScoreFormat.POINT_3 -> 3f
        ScoreFormat.POINT_5 -> 5f
        ScoreFormat.POINT_10 -> 10f
        ScoreFormat.POINT_10_DECIMAL -> 10f
        ScoreFormat.POINT_100 -> 100f
    }
}

fun convertScore(score:Float, target:ScoreFormat): Float {
    val targetMax = getMaxScore(target)
    if (score>targetMax){
        val coerced = score.coerceIn(0f, 100f)
        val sourceMax = if (coerced>10){
            100f
        } else if (coerced>5){
            10f
        } else if (coerced>3){
            5f
        } else {
            3f
        }
        val converted = coerced/sourceMax*targetMax
        return if (target!=ScoreFormat.POINT_10_DECIMAL){
            converted.roundToInt().toFloat()
        } else {
            converted
        }
    } else {
        return score
    }

}

//@Composable
//fun test(){
//    TextField(value=score, onValueChange = {
//        validScore = it.toFloatOrNull()!=null
//        score = it
//    }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScoreEditDialog(onDismissRequest:()->Unit, onConfirm:()->Unit, isValid:Boolean ,selectorWidget:@Composable ()->Unit){
    AlertDialog(onDismissRequest = onDismissRequest) {
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
                selectorWidget()
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = onConfirm, enabled = isValid) {
                        Text(text = "Confirm")
                    }
                }

            }
        }
    }
}

@Composable
fun DialogPoint5(initialValue:Int, onDismissRequest:()->Unit, onConfirm:(Float)->Unit){
    var value by remember {
        mutableStateOf(initialValue)
    }
    DialogBox("Edit Score", onDismissRequest, { onConfirm(value.toFloat()) }, true) {
        Row(horizontalArrangement = Arrangement.spacedBy(
            10.dp
        )) {

            repeat(5){
                val image by remember {
                    derivedStateOf{
                        if (value >= it+1){
                            Icons.Filled.Star
                        } else { Icons.Outlined.Star }
                    }
                }
                IconButton(onClick = {
                    value = if (value==it+1){
                        it
                    } else {
                        it + 1
                    }
                }) {
                    Icon(
                        imageVector = image,
                        contentDescription = ""
                    )
                    Text(text = image.name)
                }
            }

        }
    }
}


@Composable
fun DialogPoint3(initialValue:Int, onDismissRequest:()->Unit, onConfirm:(Float)->Unit){
    var value by remember {
        mutableStateOf(initialValue)
    }
    val iconMapping = mapOf(
        1 to  PhosphorIcons.smiley_sad(),
        2 to PhosphorIcons.smiley_meh(),
        3 to PhosphorIcons.smiley()
    )
    DialogBox("Edit Score", onDismissRequest, { onConfirm(value.toFloat()) }, true) {
        Row(horizontalArrangement = Arrangement.spacedBy(
            10.dp
        )) {
            repeat(3){
                iconMapping[it+1]?.let { it1 ->
                    Point3Radio(
                        onClick = {
                            value = if (value==it+1){
                                0
                            } else {
                                it + 1
                            }
                        },
                        selected = value==it+1,
                        icon = it1,
                    )
                }
            }

        }
    }
}

@Composable
fun RowScope.Point3Radio(onClick: () -> Unit, selected:Boolean, icon: ImageVector){
    Surface(onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.weight(1f)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = ""
            )
            RadioButton(selected = selected, onClick = onClick)
//            Text(text = text)
        }

    }
}

@Composable
fun DialogPoint10(initialValue:Int, onDismissRequest:()->Unit, onConfirm:(Float)->Unit){
    var value by remember {
        mutableStateOf("$initialValue")
    }
    val isValid by remember {
        derivedStateOf {
            value.toIntOrNull()!=null&&value.toInt()<=10&&value.toInt()>=0
        }
    }
    DialogBox("Edit Score", onDismissRequest, { onConfirm(value.toFloat()) }, isValid) {
        TextField(value = value, onValueChange = {
            value = it
        }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
    }
}

@Composable
fun DialogPoint100(initialValue:Int, onDismissRequest:()->Unit, onConfirm:(Float)->Unit){
    var value by remember {
        mutableStateOf("$initialValue")
    }
    val isValid by remember {
        derivedStateOf {
            value.toIntOrNull()!=null&&value.toInt()<=100&&value.toInt()>=0
        }
    }
    DialogBox("Edit Score", onDismissRequest, { onConfirm(value.toFloat()) }, isValid) {
        TextField(value = value, onValueChange = {
            value = it
        }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
    }
}

@Composable
fun DialogPoint10Decimal(initialValue:Float, onDismissRequest:()->Unit, onConfirm:(Float)->Unit){
    var value by remember {
        mutableStateOf("$initialValue")
    }
    val isValid by remember {
        derivedStateOf {
            value.toFloatOrNull()!=null&&value.toFloat()<=100f&&value.toFloat()>=0f
        }
    }
    DialogBox("Edit Score", onDismissRequest, { onConfirm(value.toFloat()) }, isValid) {
        TextField(value = value, onValueChange = {
            value = it
        }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal))
    }
}