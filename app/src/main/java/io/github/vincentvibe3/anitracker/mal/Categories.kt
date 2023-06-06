package io.github.vincentvibe3.anitracker.mal

enum class Categories(val color: Long?) {
    WATCHING(0xFF3DC028),
    COMPLETED(0xFF3D71BE),
    ON_HOLD(0xFFF2BD00),
    DROPPED(0xFFB72E2E),
    PLAN_TO_WATCH(0xFF7E7E7E),
    CUSTOM(null)
}