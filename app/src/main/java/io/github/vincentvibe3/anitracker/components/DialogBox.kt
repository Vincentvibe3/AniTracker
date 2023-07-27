package io.github.vincentvibe3.anitracker.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogBox(title:String, onDismissRequest:()->Unit, onConfirm:()->Unit, isValid:Boolean, onConfirmColor: ButtonColors=ButtonDefaults.buttonColors(), contents:@Composable ()->Unit){
    AlertDialog(onDismissRequest = onDismissRequest) {
        Card(colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)) {
            Column(
                Modifier
                    .padding(20.dp)
                    .fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    modifier = Modifier.padding(bottom = 10.dp),
                    text = title,
                    maxLines = 1,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
                contents()
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.End)) {
                    TextButton(onClick = onDismissRequest) {
                        Text(text = "Cancel")
                    }
                    Button(onClick = onConfirm, enabled = isValid, colors = onConfirmColor) {
                        Text(text = "Confirm")
                    }
                }

            }
        }
    }
}