package eu.kleiver.epicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.frame_imageview.view.*

class ImageActivity : AppCompatActivity() {
    private lateinit var image: ImageView
    private lateinit var link: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        link = intent.getStringExtra("LINK").toString()
        image = findViewById(R.id.fullImage)
        Glide.with(this).load(link).into(image)
    }
}