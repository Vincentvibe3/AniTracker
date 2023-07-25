package io.github.vincentvibe3.anitracker.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import io.github.vincentvibe3.anitracker.ui.theme.AniTrackerTheme

fun openLogin(context: Context){
    val url =
        "https://anilist.co/api/v2/oauth/authorize?client_id=13282&response_type=token"
    val intent = CustomTabsIntent.Builder()
        .build()
    intent.intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.launchUrl(context, Uri.parse(url))
}

@Composable
fun SetupScreen() {
    val activity = LocalContext.current as Activity
    Column(
        modifier= Modifier.fillMaxHeight().fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Setup",
        )
        Button(onClick = {
            openLogin(activity)
        }) {
            Text(text = "Login to Anilist")
        }
    }

}

@Preview
@Composable
fun SetupPreview(){
    AniTrackerTheme {
        SetupScreen()
    }
}
