package eu.kleiver.epicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.bumptech.glide.Glide

/**
 * A ViewModel that make able to observe a Boolean.
 * Used to observe if an image is favorited or not.
 */
class BooleanViewModel : ViewModel() {
    var requesting: Boolean = false
    val favorited: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    /**
     * Function that check if an image is favorited.
     *
     * @param link The link of the image to check.
     */
    fun isFavorited(link: String) {
        if (requesting)
            return
        requesting = true
        ImgurAPI.getUserFavorites(0, { receivedData ->
            if (receivedData.getBoolean("success")) {
                val images: ArrayList<Image> = ImgurAPI.jsonToImageList(receivedData.getJSONArray("data"), useFastLink = true)
                images.forEach { image ->
                    if (image.link == link) {
                        requesting = false
                        return@getUserFavorites favorited.postValue(true)
                    }
                }
            }
            favorited.postValue(false)
            requesting = false
        }, {
            favorited.postValue(false)
            requesting = false
        })
    }

    /**
     * Function that favorite an image.
     *
     * @param link The link of the image to favorite.
     */
    fun favorite(link: String) {
        if (requesting)
            return
        requesting = true
        ImgurAPI.favorite(link, { receivedData ->
            if (receivedData.getBoolean("success")) {
                favorited.postValue(receivedData.getString("data") == "favorited")
            }
            requesting = false
        }, {
            favorited.postValue(null)
            requesting = false
        })
    }
}

/**
 * The activity that shows the image in fullscreen.
 * You can like the image on this activity.
 */
class ImageActivity : AppCompatActivity() {
    private lateinit var image: ImageView
    private lateinit var favBtn: ImageView
    private lateinit var link: String
    private val favModel: BooleanViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        link = intent.getStringExtra("LINK").toString()
        image = findViewById(R.id.fullImage)
        favBtn = findViewById(R.id.fav_button)
        Glide.with(this).load(link).into(image)
        favModel.favorited.observe(this, Observer<Boolean> { liked ->
            if (liked != null) {
                favBtn.setImageResource(if (liked) {R.drawable.ic_like} else {R.drawable.ic_unliked})
            }
        })
        favBtn.setOnClickListener {
            favModel.favorite(link)
        }
        favModel.isFavorited(link)
    }
}