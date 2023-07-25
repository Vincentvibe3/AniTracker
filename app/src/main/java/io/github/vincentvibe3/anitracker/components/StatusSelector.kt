package io.github.vincentvibe3.anitracker.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusSelector(onSelected:(String)->Unit){
    val tabIndex by remember {
        mutableStateOf(1)
    }
    val categoriesMAL = listOf("Watching", "Complete", "On Hold", "Dropped", "Plan to Watch")
    val categoriesAnilist = listOf("Watching", "Complete", "On Hold", "Dropped", "Plan to Watch", "Rewatching")
    var selected by remember {
        mutableStateOf("Complete")
    }
    val categories by remember {
        derivedStateOf {
            if (tabIndex == 0) {
                categoriesAnilist
            } else {
                categoriesMAL
            }
        }
    }
    var dialogShown by remember {
        mutableStateOf(false)
    }

    Button(onClick = { dialogShown=!dialogShown },
        Modifier
            .fillMaxWidth(), shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = selected)
    }
    if (dialogShown){
        AlertDialog(onDismissRequest = { dialogShown=false }) {
            Card(
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
                modifier = Modifier.wrapContentHeight()
            ) {
                Column(
                    Modifier
                        .padding(20.dp)
                        .wrapContentHeight()
                        .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(bottom = 10.dp),
                        text = "Edit Status",
                        maxLines = 1,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                    for (category in categories) {
                        Surface(modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(10.dp))
                            .clickable {
                                selected = category
                            }, shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = selected==category, onClick = {
                                    selected = category
                                })
                                Text(text = category)
                            }
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)) {
                        TextButton(onClick = { dialogShown=!dialogShown }) {
                            Text(text = "Cancel")
                        }
                        Button(onClick = {
                            dialogShown = false
                            onSelected(selected)
                        }){
                            Text(text = "Confirm")
                        }
                    }
                }
            }
        }
    }
}