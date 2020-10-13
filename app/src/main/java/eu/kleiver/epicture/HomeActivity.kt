package eu.kleiver.epicture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ScrollView
import android.widget.TextView
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    override fun onResume() {
        super.onResume()
    }
}