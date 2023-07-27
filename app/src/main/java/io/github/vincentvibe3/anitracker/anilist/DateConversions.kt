package io.github.vincentvibe3.anitracker.anilist

import io.github.vincentvibe3.anitraklib.anilist.serialization.FuzzyDateInt
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun FuzzyDateInt.toLocalDate(): LocalDate {
    val month = this.month ?: 1
    val day = this.day ?: 1
    val year = this.year ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    return LocalDate(year, month, day)
}

fun LocalDate.toFuzzyDate(): FuzzyDateInt {
    return FuzzyDateInt(this.year, this.monthNumber, this.dayOfMonth)
}