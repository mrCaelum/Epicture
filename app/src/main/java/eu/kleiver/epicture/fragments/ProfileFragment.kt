package eu.kleiver.epicture.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.kleiver.epicture.Image
import eu.kleiver.epicture.ImgurAPI
import eu.kleiver.epicture.MainAdapter
import eu.kleiver.epicture.R
import org.json.JSONArray
import org.json.JSONObject

class ProfileFragment : Fragment() {
    private lateinit var fragView: View
    private lateinit var recyclerView: RecyclerView
    private var images: ArrayList<Image> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        refreshImages()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragView = inflater.inflate(R.layout.fragment_profile, container, false)
        recyclerView = fragView.findViewById(R.id.imagesRecyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(fragView.context)
        recyclerView.adapter = MainAdapter(images)
        return fragView
    }

    private fun refreshImages() {
        ImgurAPI.getUserImages(0, { receivedData ->
            val jsonData: JSONArray = receivedData.getJSONArray("data") ?: return@getUserImages
            images.clear()
            for (i in 0 until jsonData.length()) {
                val jsonImage: JSONObject = jsonData.getJSONObject(i)
                images.add(Image(
                    id = jsonImage.getString("id"),
                    title = jsonImage.getString("title"),
                    description = jsonImage.getString("description"),
                    animated = jsonImage.getString("animated").toBoolean(),
                    width = jsonImage.getString("width").toInt(),
                    height = jsonImage.getString("height").toInt(),
                    size = jsonImage.getString("size").toInt(),
                    favorite = jsonImage.getString("favorite").toBoolean(),
                    link = jsonImage.getString("link")
                ))
            }
        }, {})
    }
}