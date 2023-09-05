package io.github.vincentvibe3.anitracker.anilist

import io.github.vincentvibe3.anitraklib.anilist.types.CharacterRole
import io.github.vincentvibe3.anitraklib.anilist.types.ExternalLinkType
import io.github.vincentvibe3.anitraklib.anilist.types.Media
import io.github.vincentvibe3.anitraklib.anilist.types.MediaList
import io.github.vincentvibe3.anitraklib.anilist.types.MediaRelation
import io.github.vincentvibe3.anitraklib.anilist.types.MediaSeason
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive

fun MediaList.toAnimeCardData(): AnimeCardData?{
    val title = this.media?.title?.english
    val score = this.score
    val progress = this.progress
    val episodes = this.media?.episodes
    val image = this.media?.coverImage?.medium
    val rewatches = this.repeat
    val notes = this.notes
    val id = this.mediaId
    val status = this.status
    val private = this.private
    val favourite = this.media?.isFavourite
    val startedAt = this.startedAt
    val completedAt = this.completedAt
    val entryCustomLists = this.customLists?.toMap()?.mapValues { pair ->
        pair.value.jsonPrimitive.boolean
    }
    return if (title != null &&
        score != null &&
        progress != null &&
        episodes != null &&
        image != null &&
        id != null &&
        rewatches != null &&
        status != null &&
        private != null &&
        favourite != null &&
        startedAt != null &&
        completedAt != null &&
        entryCustomLists != null
    ) {
        AnimeCardData(
            title,
            score,
            progress,
            episodes,
            image,
            id,
            status,
            rewatches,
            entryCustomLists,
            notes,
            private,
            favourite,
            startedAt.toLocalDate(),
            completedAt.toLocalDate()
        )
    } else {
        null
    }
}

fun Media.toMediaViewData(scoreFormat: ScoreFormat): MediaViewData? {
    val id = this.id
    val title = this.title?.userPreferred
    val score = this.averageScore
    val image = this.coverImage?.large
    val mediaFormat = this.format
    val episodeCount = this.episodes
    val episodeLength = this.duration
    val studios = this.studios?.nodes?.filter{
        it.isAnimationStudio==true
    }?.mapNotNull {
        it.name
    }
    val sourceFormat = this.source
    val genres = this.genres
    val season = if (this.seasonYear!=null&&this.season!=null){
        when(this.season){
            MediaSeason.FALL -> "Fall ${this.seasonYear}"
            MediaSeason.SPRING -> "Spring ${this.seasonYear}"
            MediaSeason.SUMMER -> "Summer ${this.seasonYear}"
            MediaSeason.WINTER -> "Winter ${this.seasonYear}"
            null -> null
        }
    } else {
        null
    }
    val favourite = this.isFavourite
    val mediaList = this.mediaListEntry
    val startDate = this.startDate
    val endDate = this.endDate
    val popularity = this.popularity?.toLong()
    val description = this.description?.replace("<br>", "") ?: ""
    val relations = this.relations?.edges?.mapNotNull {
        val relationTitle = it.node.title?.userPreferred
        val relationImage = it.node.coverImage?.medium
        val relationType = when(it.relationType){
            MediaRelation.SOURCE -> "Source"
            MediaRelation.ADAPTATION -> "Adaptation"
            MediaRelation.ALTERNATIVE -> "Alternative"
            MediaRelation.CHARACTER -> "Character"
            MediaRelation.COMPILATION -> "Compilation"
            MediaRelation.CONTAINS -> "Contains"
            MediaRelation.OTHER -> "Other"
            MediaRelation.PARENT -> "Parent"
            MediaRelation.PREQUEL -> "Prequel"
            MediaRelation.SEQUEL -> "Sequel"
            MediaRelation.SIDE_STORY -> "Side Story"
            MediaRelation.SPIN_OFF -> "Spin-off"
            MediaRelation.SUMMARY -> "Summary"
        }
        val relationFormat = it.node.format
        if (relationTitle!=null&&relationImage!=null&&relationFormat!=null) {
            MediaViewData.Relation(relationTitle, relationImage, relationType, relationFormat)
        } else {
            null
        }
    }
    val recommendations = this.recommendations?.nodes?.mapNotNull {
        val recommendationTitle = it.mediaRecommendation?.title?.userPreferred
        val recommendationImage = it.mediaRecommendation?.coverImage?.medium
        if (recommendationTitle != null && recommendationImage != null) {
            MediaViewData.Recommendation(recommendationTitle, recommendationImage)
        } else {
            null
        }
    }
    val characters = this.characters?.edges?.mapNotNull {
        val characterName = it.node.name?.userPreferred
        val charImage = it.node.image?.medium
        val role = when(it.role){
            CharacterRole.BACKGROUND -> "Background"
            CharacterRole.MAIN -> "Main"
            CharacterRole.SUPPORTING -> "Supporting"
        }
        val voiceActor = it.voiceActors.firstOrNull().let { voiceActor ->
            if (voiceActor!=null){
                "${voiceActor.languageV2}: ${voiceActor.name.userPreferred}"
            } else {
                ""
            }
        }
        if (charImage!=null&&characterName!=null){
            MediaViewData.MediaCharacterEntry(characterName, charImage, role, voiceActor)
        } else {
            null
        }
    }
    val staff = this.staff?.edges?.mapNotNull {
        val staffName = it.node.name.userPreferred
        val staffImage = it.node.image?.medium
        val role = it.role
        if (staffName != null && staffImage != null) {
            MediaViewData.MediaStaff(staffName, staffImage, role)
        } else {
            null
        }
    }
    val tags = this.tags?.map {
        it.name
    }
    val externalLinks = this.externalLinks?.filter{
        it.type != ExternalLinkType.STREAMING
    }?.map {
        MediaViewData.ExternalResource(it.site, it.url, it.icon)
    }
    val streaming = this.externalLinks?.filter {
        it.type == ExternalLinkType.STREAMING
    }?.map {
        MediaViewData.ExternalResource(it.site, it.url, it.icon)
    }
    return if(
        id!=null&&
        title!=null&&
        score!=null&&
        image!=null&&
        mediaFormat!=null&&
        episodeCount!=null&&
        episodeLength!=null&&
        studios!=null&&
        genres!=null&&
        season!=null&&
        startDate!=null&&
        endDate!=null&&
        popularity!=null&&
        relations!=null&&
        recommendations!=null&&
        characters!=null&&
        staff!=null&&
        sourceFormat!=null&&
        externalLinks!=null&&
        streaming!=null&&
        tags != null&&
        favourite != null
    ){
        MediaViewData(
            mediaId = id,
            title = title,
            score = score,
            image = image,
            scoreFormat = scoreFormat,
            mediaFormat = mediaFormat,
            episodeCount = episodeCount,
            episodeLength = episodeLength,
            studioNames = studios,
            genres = genres,
            sourceFormat = sourceFormat,
            season = season,
            startDate = startDate,
            endDate = endDate,
            popularity = popularity,
            description = description,
            relations = relations,
            recommendations = recommendations,
            characters = characters,
            externalLinks = externalLinks,
            staff = staff,
            tags = tags,
            streamingSources = streaming,
            favourite = favourite,
            listInfo = mediaList?.toAnimeCardData()
        )
    } else {
        null
    }
}