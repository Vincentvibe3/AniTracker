package io.github.vincentvibe3.anitracker.components

import android.os.Build
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(){
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var date by remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mutableStateOf(Date.from(Instant.now()))
        } else {
            mutableStateOf(Date(System.currentTimeMillis()/1000))
        }
    }
    val datePickerState = rememberDatePickerState()
    if(showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker=false },
            confirmButton = {
                Button(onClick = {
                    date = datePickerState.selectedDateMillis?.let { Date(it) }
                    showDatePicker=false
                }) {
                    Text(text = "Done")
                }
            }) {

            DatePicker(
                state = datePickerState,
            )
        }
    }
    Button(onClick = { showDatePicker=true }) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Text(text = date.toInstant().atZone(ZoneId.of("UTC")).toLocalDate().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        } else {
            val dateFormat by remember {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                mutableStateOf(dateFormat)
            }

            Text(text = dateFormat.format(date.time))
        }
    }
}