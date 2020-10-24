package eu.kleiver.epicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import androidx.activity.viewModels

class ProfileActivity : AppCompatActivity() {

    private lateinit var usernameContainer: TextView
    private lateinit var avatarContainer2: ImageView
    private val avatarModel: AvatarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        usernameContainer = findViewById(R.id.username_container)
        avatarContainer2 = findViewById(R.id.avatar_container2)

        usernameContainer.text = ImgurAPI.data["account_username"]
        avatarModel.url.observe(this, Observer<String> { newAvatar ->
            if (newAvatar != null)
                Glide.with(this).load(newAvatar).into(avatarContainer2)
        })
        avatarModel.loadAvatar()
    }
}