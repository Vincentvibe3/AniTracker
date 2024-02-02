package io.github.vincentvibe3.anitracker.anilist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.github.vincentvibe3.anitraklib.anilist.serialization.FuzzyDateInt
import io.github.vincentvibe3.anitraklib.anilist.types.ExternalLinkType
import io.github.vincentvibe3.anitraklib.anilist.types.MediaFormat
import io.github.vincentvibe3.anitraklib.anilist.types.MediaSource
import io.github.vincentvibe3.anitraklib.anilist.types.ScoreFormat

class MediaViewModel:ViewModel() {

    val mediaData:MediaViewData?
        get() = _mediaData.value

    private val _mediaData = mutableStateOf<MediaViewData?>(null)

    suspend fun loadData(id:Int) : MediaViewData? {
        val rawData = Clients.anilistClient.getMedia(id)
        return rawData?.let {
            MediaViewData(
                mediaId = rawData.id ?: 0,
                title = rawData.title?.userPreferred ?: "",
                image = rawData.coverImage?.medium ?: "",
                score = rawData.averageScore ?: 0,
                scoreFormat = ScoreFormat.POINT_10,
                mediaFormat = rawData.format ?: MediaFormat.TV,
                episodeCount = rawData.episodes ?: 0,
                episodeLength = rawData.duration ?: 0,
                studioNames = rawData.studios?.nodes?.mapNotNull { it.name } ?: listOf(),
                genres = rawData.genres ?: listOf(),
                sourceFormat = rawData.source ?: MediaSource.MANGA,
                season = rawData.season?.name ?: "",
                startDate = rawData.startDate ?: FuzzyDateInt(0, null, null),
                endDate = rawData.endDate ?: FuzzyDateInt(0, null, null),
                popularity = rawData.popularity?.toLong() ?: 0,
                description = rawData.description ?: "",
                relations = rawData.relations?.edges?.map {
                    MediaViewData.Relation(
                        it.node.title?.userPreferred ?: "",
                        it.node.coverImage?.medium ?: "",
                        it.relationType.toString() ?: "",
                        it.node.format ?: MediaFormat.TV
                    )
                } ?: listOf(),
                recommendations = rawData.recommendations?.edges?.map {
                    MediaViewData.Recommendation(
                        it.node.mediaRecommendation?.title?.userPreferred ?: "",
                        it.node.mediaRecommendation?.coverImage?.medium ?: ""
                    )
                } ?: listOf(),
                characters = rawData.characters?.edges?.map {
                    MediaViewData.MediaCharacterEntry(
                        it.name ?: "",
                        it.node.image?.medium ?: "",
                        it.role.toString(),
                        it.voiceActors.first().name.userPreferred ?: ""
                    )
                } ?: listOf(),
                staff = rawData.staff?.edges?.map {
                    MediaViewData.MediaStaff(
                        it.node.name.userPreferred ?: "",
                        it.node.image?.medium ?: "",
                        it.role
                    )
                } ?: listOf(),
                tags = rawData.tags?.map { it.name } ?: listOf(),
                externalLinks = rawData.externalLinks?.filter { it.type != ExternalLinkType.STREAMING }
                    ?.map {
                        MediaViewData.ExternalResource(
                            it.site,
                            it.url,
                            it.icon
                        )
                    } ?: listOf(),
                streamingSources = rawData.externalLinks?.filter { it.type == ExternalLinkType.STREAMING }
                    ?.map {
                        MediaViewData.ExternalResource(
                            it.site,
                            it.url,
                            it.icon
                        )
                    } ?: listOf(),
                favourite = rawData.isFavourite ?: false,
            )
        }
    }

}