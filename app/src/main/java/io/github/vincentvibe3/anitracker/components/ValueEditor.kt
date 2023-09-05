package io.github.vincentvibe3.anitracker.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ValueEditor(initialValue:Int, lowerBound:Int?=null, upperBound:Int?=null, onChange:(Int)->Unit){
    var text by remember {
        mutableStateOf("$initialValue")
    }
    var lastValid by remember {
        mutableStateOf(text)
    }
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val valid by remember {
        derivedStateOf {
            val converted = text.toIntOrNull()
            if (converted==null){
                false
            } else {
                val lowerCondition = if (lowerBound!=null){
                    converted>=lowerBound
                } else {
                    true
                }
                val upperCondition = if (upperBound!=null){
                    converted<=upperBound
                } else {
                    true
                }
                lowerCondition&&upperCondition
            }
        }
    }
    val borderColor by remember {
        derivedStateOf {
            when {
                isFocused&&!valid-> Color(0xFFb72e2e)
                isFocused&&valid-> Color(0xFF3dc028)
                else -> Color(0xFF7e7e7e)
            }
        }
    }
    if (!isFocused&&!valid){
        LaunchedEffect(key1 = Unit){
            launch {
                text = lastValid
            }
        }
    }
    val borderModifier by remember {
        derivedStateOf {
            if (isFocused){
                Modifier.border(1.dp, borderColor, RoundedCornerShape(10.dp))
            } else {
                Modifier
            }
        }
    }
    val visualTransformation = if (upperBound!=null){
        VisualTransformation {
            TransformedText(AnnotatedString("${it.text}/$upperBound"), object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return offset
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return if (offset > text.length) {
                        text.length
                    } else {
                        offset
                    }
                }

            })
        }
    } else {
        VisualTransformation.None
    }
    Row(
        Modifier
            .wrapContentHeight()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        QuickAccessButton(onClick = {
            text = "${text.toInt()-1}"
            lastValid = text
        }, icon = Icons.Default.KeyboardArrowDown, enabled = if (valid){
            if (lowerBound!=null){
                lowerBound<text.toInt()
            } else {
                true
            }
        } else {
            false
        })
        BasicTextField(
            value = text,{
                text = it
                if (valid){
                    lastValid = text
                    onChange(text.toInt())
                }
            },
            interactionSource=interactionSource,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            visualTransformation = visualTransformation,
            textStyle = TextStyle.Default.copy(textAlign = TextAlign.Center, fontWeight = FontWeight.Bold),
            singleLine = true,
            modifier= Modifier
                .weight(1f)
                .fillMaxHeight()
                .then(borderModifier)
                .background(Color(0xFFD4D4D4), RoundedCornerShape(10.dp))
        ){
            Row(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                it()
            }
        }
        QuickAccessButton(onClick = {
            text = "${text.toInt()+1}"
            lastValid = text
        }, icon = Icons.Default.KeyboardArrowUp, enabled = if (valid){
            if (upperBound!=null){
                upperBound>text.toInt()
            } else {
                true
            }
        } else {
            false
        })
    }
}