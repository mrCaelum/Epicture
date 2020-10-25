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

class FavoritesFragment : Fragment() {
    private lateinit var fragView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var refreshItem: SwipeRefreshLayout
    private var images: ArrayList<Image> = ArrayList()
    private lateinit var adapter: MainAdapter
    private var requesting: Boolean = false

    private fun initRecyclerView() {
        adapter = MainAdapter(images)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(false)
        recyclerView.layoutManager = LinearLayoutManager(fragView.context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragView = inflater.inflate(R.layout.fragment_favorites, container, false)
        recyclerView = fragView.findViewById(R.id.favoritesRecyclerView)
        refreshItem = fragView.findViewById(R.id.favoritesRefreshItem)
        initRecyclerView()
        refreshItem.setOnRefreshListener {
            refreshImages()
        }
        refreshImages()
        return fragView
    }

    override fun onResume() {
        super.onResume()
        recyclerView.scrollToPosition(0)
    }

    private fun refreshImages() {
        if (requesting)
            return
        requesting = true
        refreshItem.isRefreshing = true
        ImgurAPI.getUserFavorites(0, { receivedData ->
            images.clear()
            images.addAll(ImgurAPI.jsonToImageList(receivedData.getJSONArray("data"), useFastLink = true))
            refreshItem.isRefreshing = false
            requesting = false
        }, {
            refreshItem.isRefreshing = false
            requesting = false
        })
    }
}