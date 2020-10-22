package eu.kleiver.epicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import eu.kleiver.epicture.fragments.*
import kotlinx.android.synthetic.main.activity_home.*

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
        val favouritesFragment = FavouritesFragment()
        val settingsFragment = SettingsFragment()
        val profileFragment = ProfileFragment()
        val searchFragment = SearchFragment()

        makeCurrentFragment(profileFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_upload -> makeCurrentFragment(uploadFragment)
                R.id.ic_favorite -> makeCurrentFragment(favouritesFragment)
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
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}
