package io.github.vincentvibe3.anitracker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SectionTitle(modifier: Modifier, text:String){
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp, start = 20.dp)
            .then(modifier), horizontalArrangement = Arrangement.Start) {
        Text(
            text = text,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp
        )
    }
}