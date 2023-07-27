package io.github.vincentvibe3.anitracker.anilist

suspend fun UpdateAnime(data:AnimeCardData) {
    val anilist = Clients.anilistClient
    println(data)
    val result = anilist.updateAnime(
        data.id,
        data.status,
        data.score,
        progress = data.progress,
        repeat = data.rewatches,
        private = data.private,
        notes = data.notes,
        customLists = data.customLists.filter { it.value }.map { it.key },
        startedAt = data.startedAt?.toFuzzyDate(),
        completedAt = data.completedAt?.toFuzzyDate()
    )
    println(result)
}