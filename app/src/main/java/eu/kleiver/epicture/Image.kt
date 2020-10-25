package eu.kleiver.epicture

/**
 * A data class that represent an Imgur image
 */
data class Image(
    val id: String,
    var is_album: Boolean,
    var link: String,
    var images: ArrayList<Image>?
)