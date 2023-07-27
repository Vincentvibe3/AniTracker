package io.github.vincentvibe3.anitracker.components

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerButton(initialDate: LocalDate?, onChange:(LocalDate?)->Unit){
    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var date by remember {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            mutableStateOf(Date.from(Instant.now()))
//        } else {
//            mutableStateOf(Date(System.currentTimeMillis()/1000))
//        }
        mutableStateOf<LocalDate?>(null)
    }
    val datePickerState = rememberDatePickerState()
    if(showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker=false },
            dismissButton = {
                TextButton(onClick = { showDatePicker=false }) {
                    Text(text = "Cancel")   
                }
            },
            confirmButton = {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    FilledTonalButton(onClick = {
                        date = null
                        showDatePicker=false
                        onChange(date)
                    }) {
                        Text(text = "No Date")
                    }
                    Button(onClick = {
                        date = datePickerState.selectedDateMillis?.let { LocalDate.fromEpochDays((it/86400000).toInt()) }
                        showDatePicker=false
                        onChange(date)
                    }) {
                        Text(text = "Done")
                    }
                }
            }) {

            DatePicker(
                state = datePickerState,
            )
        }
    }
    val dateText by remember {
        derivedStateOf {
            val tempDate = date
            if (tempDate != null) {
                "${tempDate.dayOfMonth}/${tempDate.monthNumber}/${tempDate.year}"
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//                    tempDate.toInstant().atZone(ZoneId.of("UTC")).toLocalDate().format(
//                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
//                    )
//                } else {
//                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//                    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
//                    dateFormat.format(tempDate.time)
//                }
            } else {
                "No Date"
            }
        }
    }
    Button(onClick = { showDatePicker=true }) {
        Text(text = dateText)
    }
}