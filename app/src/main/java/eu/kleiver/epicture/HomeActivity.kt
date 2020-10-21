package eu.kleiver.epicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import eu.kleiver.epicture.fragments.*
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}
