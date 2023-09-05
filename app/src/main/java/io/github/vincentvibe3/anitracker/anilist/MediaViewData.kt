package io.github.vincentvibe3.anitracker.anilist

import io.github.vincentvibe3.anitraklib.anilist.serialization.FuzzyDateInt
import io.github.vincentvibe3.anitraklib.anilist.types.MediaFormat
import io.github.vincentvibe3.anitraklib.anilist.types.MediaSource
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat

data class MediaViewData(
    val mediaId:Int,
    val title:String,
    val image:String,
    val score:Int,
    val scoreFormat: ScoreFormat,
    val mediaFormat: MediaFormat,
    val episodeCount:Int,
    val episodeLength:Int,
    val studioNames:List<String>,
    val genres:List<String>,
    val sourceFormat: MediaSource,
    val season:String,
    val startDate:FuzzyDateInt,
    val endDate:FuzzyDateInt,
    val popularity:Long,
    val description:String,
    val relations:List<Relation>,
    val recommendations:List<Recommendation>,
    val characters:List<MediaCharacterEntry>,
    val staff:List<MediaStaff>,
    val tags:List<String>,
    val externalLinks:List<ExternalResource>,
    val streamingSources:List<ExternalResource>,
    val favourite:Boolean,
    val listInfo:AnimeCardData?=null
){

    data class ExternalResource(
        val name:String,
        val url:String,
        val image:String?
    )

    data class MediaStaff(
        val name:String,
        val image:String,
        val staffRole:String
    )

    data class Relation(
        val name:String,
        val image:String,
        val relationType:String,
        val mediaFormat: MediaFormat
    )

    data class Recommendation(
        val name:String,
        val image:String,
    )

    data class MediaCharacterEntry(
        val name:String,
        val image:String,
        val characterRole:String,
        val voiceActor: String
    )
}