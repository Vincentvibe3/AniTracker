package io.github.vincentvibe3.anitracker.anilist

import io.github.vincentvibe3.anitraklib.anilist.types.MediaListStatus

object CategoryColors {

    val colors = hashMapOf(
        MediaListStatus.COMPLETED to 0xFF3D71BE,
        MediaListStatus.DROPPED to 0xFFB72E2E,
        MediaListStatus.CURRENT to 0xFF3DC028,
        MediaListStatus.PAUSED to 0xFFF2BD00,
        MediaListStatus.PLANNING to 0xFF7E7E7E,
        MediaListStatus.REPEATING to 0xFF3DC028
    )

}