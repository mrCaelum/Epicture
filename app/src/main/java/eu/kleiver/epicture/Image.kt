package eu.kleiver.epicture

data class Image (
    val id: String,
    val title: String,
    val description: String,
    val animated: Boolean,
    val width: Int,
    val height: Int,
    val size: Int,
    val favorite: Boolean,
    val link: String
)