package eu.kleiver.epicture.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.kleiver.epicture.Image
import eu.kleiver.epicture.R

class SearchFragment : Fragment() {
    private lateinit var fragView: View
    private lateinit var recyclerView: RecyclerView
    private var images: ArrayList<Image> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }
}