package eu.kleiver.epicture

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

/**
 * The activity that makes able the user to sign in to Imgur.
 */
class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val loginButton = findViewById<Button>(R.id.login_button)
        loginButton?.setOnClickListener {
            ImgurAPI.askCredentials(this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        when (intent?.action) {
            Intent.ACTION_VIEW -> {
                intent.dataString?.let {
                    ImgurAPI.parseData(it)
                    startActivity(Intent(this, HomeActivity::class.java))
                }
            }
        }
    }
}