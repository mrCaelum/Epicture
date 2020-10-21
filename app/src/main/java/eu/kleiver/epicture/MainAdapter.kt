package eu.kleiver.epicture

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.frame_imageview.view.*

class MainAdapter(private val dataset: ArrayList<Image>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.frame_imageview, parent, false) as FrameLayout
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.view).load(dataset[position].link).into(holder.view.imageView)
    }

    override fun getItemCount() = dataset.size
}