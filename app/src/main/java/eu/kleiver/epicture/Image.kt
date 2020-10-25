package eu.kleiver.epicture

data class Image(
    val id: String,
    val title: String,
    val is_album: Boolean,
    val link: String,
    var images: ArrayList<Image>?
)