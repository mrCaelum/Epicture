package eu.kleiver.epicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import androidx.activity.viewModels
import com.google.android.material.textfield.TextInputEditText

/**
 * The profile activity of the logged user.
 */
class ProfileActivity : AppCompatActivity() {

    private lateinit var usernameContainer: TextView
    private lateinit var avatarContainer2: ImageView
    private lateinit var userBio: TextInputEditText

    private val avatarModel: AvatarViewModel by viewModels()
    private val bioModel: BioViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        usernameContainer = findViewById(R.id.username_container)
        avatarContainer2 = findViewById(R.id.avatar_container2)
        userBio = findViewById(R.id.user_bio)

        usernameContainer.text = ImgurAPI.data["account_username"]
        avatarModel.url.observe(this, Observer<String> { newAvatar ->
            if (newAvatar != null)
                Glide.with(this).load(newAvatar).into(avatarContainer2)
        })
        avatarModel.loadAvatar()

//        bioModel.loadBio()
//        bioModel.url.observe(this, Observer<String> { newBio ->
//            userBio.text
//        })
    }
}