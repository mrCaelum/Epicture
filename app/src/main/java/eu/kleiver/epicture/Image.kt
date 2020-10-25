package eu.kleiver.epicture

data class Image(
    val id: String,
    var is_album: Boolean,
    var link: String,
    var images: ArrayList<Image>?
)