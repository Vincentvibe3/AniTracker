package io.github.vincentvibe3.anitracker.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
)
@Composable
fun CustomListSelector(initialListStates:Map<String, Boolean>, onChange:(String, Boolean)->Unit) {
    val map = remember {
        val map = mutableStateMapOf<String, Boolean>()
        initialListStates.forEach {
            map[it.key] = it.value
        }
        map
    }
    var newListDialogOpen by remember {
        mutableStateOf(false)
    }
    Column {
        FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            for (entry in map){
                map[entry.key]?.let{
                    FilterChip(
                        selected = it,
                        onClick = {
                            map[entry.key] = !it
                            onChange(entry.key, !it)
                        },
                        label = { Text(
                            text = entry.key
                        ) },
                        trailingIcon = {
                            AnimatedVisibility(visible = it) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                            }
                    })
                }
            }
            FilterChip(
                selected = false,
                onClick = {
                    newListDialogOpen=true
                },
                label = { Text(
                    text = "New Custom List"
                ) },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null)
                })

        }

//        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
//            Button(onClick = { newListDialogOpen=true }) {
//                Text(text = "New Custom List")
//            }
//            TextButton(onClick = { expanded=!expanded }) {
//                Text(text = "Expand")
//                Icon(imageVector = if (!expanded){Icons.Default.KeyboardArrowDown} else {Icons.Default.KeyboardArrowUp}, contentDescription = null)
//            }
//        }
        if (newListDialogOpen){
            var newListName by remember {
                mutableStateOf("")
            }
            DialogBox(
                title = "New Custom list",
                onDismissRequest = { newListDialogOpen=false },
                onConfirm = {
                    /*TODO: Send request*/
                    map[newListName] = false
                    onChange(newListName, false)
                    newListDialogOpen = false
                },
                isValid = true
            ) {
                TextField(value = newListName, onValueChange = {
                    newListName = it
                }, label = {
                    Text(text = "New List Name")
                })
            }
        }

    }
}

@Preview
@Composable
fun CustomListSelectorPreview(){
    CustomListSelector(mapOf("text" to false, "text2" to false)){_,_->}
}