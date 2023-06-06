package io.github.vincentvibe3.anitracker.mal

class ImageResource(
    val type:ImageType,
    val location:String
) {
    enum class ImageType{
        NETWORK, RESOURCE
    }
}