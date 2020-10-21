package eu.kleiver.epicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import eu.kleiver.epicture.fragments.*
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import android.view.Menu

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uploadFragment = UploadFragment()
        val favouritesFragment = FavouritesFragment()
        val settingsFragment = SettingsFragment()
        val profileFragment = ProfileFragment()
        val searchFragment = SearchFragment()

        makeCurrentFragment(profileFragment)

        setContentView(R.layout.activity_home)
        findViewById<TextView>(R.id.text_username).text = "Welcome back, " + ImgurAPI.data["account_username"] + " !"
        ImgurAPI.getUserImages(0)
        val imagesView = findViewById<ScrollView>(R.id.images_view)
        val imagesArray = ImgurAPI.receivedData?.getJSONArray("data")
        imagesArray?.let {
            for (i in 0 until it.length()) {
                val image: JSONObject = it.getJSONObject(i)

            }
        }

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

        bottom_navigation.setSelectedItemId(R.id.ic_profile);

    }
    
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}
