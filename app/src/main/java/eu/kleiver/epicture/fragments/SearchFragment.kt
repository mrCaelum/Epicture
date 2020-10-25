package eu.kleiver.epicture.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import eu.kleiver.epicture.Image
import eu.kleiver.epicture.ImgurAPI
import eu.kleiver.epicture.MainAdapter
import eu.kleiver.epicture.R
import org.json.JSONArray
import org.json.JSONObject

class SearchFragment : Fragment() {
    private lateinit var fragView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshItem: SwipeRefreshLayout
    private lateinit var searchBar: EditText
    private lateinit var searchBtn: ImageButton
    private var images: ArrayList<Image> = ArrayList()
    private lateinit var adapter: MainAdapter
    private var requesting: Boolean = false

    private fun initRecyclerView() {
        adapter = MainAdapter(images)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(fragView.context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragView = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = fragView.findViewById(R.id.searchRecyclerView)
        refreshItem = fragView.findViewById(R.id.searchRefreshItem)
        searchBar = fragView.findViewById(R.id.search_bar)
        searchBtn = fragView.findViewById(R.id.search_button)
        initRecyclerView()
        searchBtn.setOnClickListener {
            refreshImages()
        }
        refreshItem.setOnRefreshListener {
            refreshImages()
        }
        refreshImages()
        return fragView
    }

    private fun refreshImages() {
        if (requesting)
            return
        requesting = true
        ImgurAPI.searchImages(searchBar.text.toString(), { receivedData ->
            val jsonArray: JSONArray = receivedData.getJSONArray("data") ?: return@searchImages
            images.clear()
            for (i in 0 until jsonArray.length()) {
                val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                val newImage = Image(
                    id = jsonObject.getString("id"),
                    title = jsonObject.getString("title"),
                    link = jsonObject.getString("link"),
                    is_album = jsonObject.getBoolean("is_album"),
                    images = null
                )
                if (newImage.is_album) {
                    val jsonSubArray: JSONArray = jsonObject.getJSONArray("images") ?: return@searchImages
                    newImage.images = ArrayList()
                    for (j in 0 until jsonSubArray.length()) {
                        val jsonSubObject: JSONObject = jsonSubArray.getJSONObject(j)
                        val newSubImage = Image(
                            id = jsonSubObject.getString("id"),
                            title = jsonSubObject.getString("title"),
                            link = jsonSubObject.getString("link"),
                            is_album = false,
                            images = null
                        )
                        newImage.images?.add(newSubImage) ?: continue
                    }
                }
                images.add(newImage)
            }
            refreshItem.isRefreshing = false
            requesting = false
        }, {
            refreshItem.isRefreshing = false
            requesting = false
        })
    }
}