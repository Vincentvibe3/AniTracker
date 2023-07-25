package io.github.vincentvibe3.anitracker.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBackPressed:()->Unit){
    var showNsfw by remember {
        mutableStateOf(false)
    }
    var systemDarkMode by remember {
        mutableStateOf(false)
    }
    var darkMode by remember {
        mutableStateOf(false)
    }
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)
    val scrollState = rememberScrollState()
    val scrollDispatcher by remember {
        mutableStateOf(NestedScrollDispatcher())
    }
    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }, navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }, scrollBehavior = scrollBehavior) }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(it)
                .nestedScroll(scrollBehavior.nestedScrollConnection, scrollDispatcher)
                .verticalScroll(scrollState), horizontalAlignment = Alignment.CenterHorizontally) {
            SettingSection(title = "Anilist Settings", true) {
                SettingItem(name = "Preferred title language", onClick = { /*TODO*/ }, description = "Controls the language to display title in")
                SettingItem(name = "Set scoring method", onClick = { /*TODO*/ }, description = "Current: 10 point decimal")
                SettingItem(name = "Hide Nsfw", onClick = { showNsfw = !showNsfw }, description = "Hides Nsfw content") {
                    Switch(checked = showNsfw, onCheckedChange = {
                        showNsfw = !showNsfw
                    })
                }
                SettingItem(name = "Log Out", onClick = { /*TODO*/ }, description = "Log out of Anilist", titleColor = Color.Red)
            }
            SettingSection(title = "MyAnimeList Settings") {
                SettingItem(name = "Preferred title language", onClick = { /*TODO*/ }, description = "Controls the language to display title in")
                SettingItem(name = "Set scoring method", onClick = { /*TODO*/ }, description = "Current: 10 point decimal")
                SettingItem(name = "Hide Nsfw", onClick = { showNsfw = !showNsfw }, description = "Hides Nsfw content") {
                    Switch(checked = showNsfw, onCheckedChange = {
                        showNsfw = !showNsfw
                    })
                }
                SettingItem(name = "Log Out", onClick = { /*TODO*/ }, description = "Log out of Anilist", titleColor = Color.Red)
            }
            SettingSection(title = "Appearance") {
                SettingItem(name = "Use system darkmode", onClick = { systemDarkMode = !systemDarkMode }, description = "Sync with system settings") {
                    Switch(checked = systemDarkMode, onCheckedChange = {
                        systemDarkMode = !systemDarkMode
                    })
                }
                SettingItem(name = "Darkmode", onClick = { darkMode = !darkMode }, description = "Turn on darkmode", enable = !systemDarkMode) {
                    Switch(checked = darkMode, onCheckedChange = {
                        darkMode = !darkMode
                    }, enabled = !systemDarkMode)
                }
                SettingItem(name = "Use dynamic colors", onClick = { darkMode = !darkMode }) {
                    Switch(checked = darkMode, onCheckedChange = {
                        darkMode = !darkMode
                    })
                }
            }

        }
    }

}

@Composable
fun SettingSection(title:String, isFirst:Boolean=false, contents: @Composable ()->Unit){
    Column {
        if (!isFirst){
            Spacer(modifier = Modifier
                .padding(vertical = 16.dp)
                .background(Color.Gray)
                .height(1.dp)
                .fillMaxWidth())
        }
        Text(text = title,
            modifier = Modifier.padding(24.dp, 16.dp),
            fontSize = 14.sp,
            color = Color.DarkGray,
            fontWeight = FontWeight.Medium
        )
        contents()
    }
}

@Composable
fun SettingItem(
    name:String,
    onClick:() -> Unit,
    description:String?=null,
    enable:Boolean=true,
    titleColor:Color=MaterialTheme.colors.onBackground,
    indicator:@Composable ()->Unit={}
){
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = true,
                onClick = {
                    if (enable) {
                        onClick()
                    }
                }
            ),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = name,
                    color=if (enable){
                        titleColor
                    }else{
                        Color.Gray
                    },
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )
                if (description!=null){
                    Text(
                        text = description,
                        color=Color.Gray,
//                    style=MaterialTheme.typography.caption
                    )
                }
            }
            indicator()
        }
    }
}