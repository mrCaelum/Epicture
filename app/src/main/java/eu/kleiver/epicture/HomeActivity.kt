package eu.kleiver.epicture

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import eu.kleiver.epicture.fragments.*
import kotlinx.android.synthetic.main.activity_home.*

/**
 * The main activity of the application.
 * The 5 fragments are implemented here.
 */
class HomeActivity : AppCompatActivity() {
    private lateinit var usernameContainer: TextView
    private lateinit var avatarContainer: ImageView
    private val avatarModel: AvatarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        usernameContainer = findViewById(R.id.username_container)
        avatarContainer = findViewById(R.id.avatar_container)

        val uploadFragment = UploadFragment()
        val favoritesFragment = FavoritesFragment()
        val settingsFragment = SettingsFragment()
        val profileFragment = ProfileFragment()
        val searchFragment = SearchFragment()

        makeCurrentFragment(profileFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_upload -> makeCurrentFragment(uploadFragment)
                R.id.ic_favorite -> makeCurrentFragment(favoritesFragment)
                R.id.ic_profile -> makeCurrentFragment(profileFragment)
                R.id.ic_search -> makeCurrentFragment(searchFragment)
                R.id.ic_settings -> makeCurrentFragment(settingsFragment)
            }
            true
        }
        bottom_navigation.selectedItemId = R.id.ic_profile
        usernameContainer.text = ImgurAPI.data["account_username"]
        avatarModel.url.observe(this, Observer<String> { newAvatar ->
            if (newAvatar != null)
                Glide.with(this).load(newAvatar).into(avatarContainer)
        })
        avatarModel.loadAvatar()

        val avatarContainer = findViewById<ImageView>(R.id.avatar_container)
        avatarContainer.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            Toast.makeText(this, "Welcome to your profile settings !", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}
