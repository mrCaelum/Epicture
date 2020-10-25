package eu.kleiver.epicture

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.frame_imageview.view.*

/**
 * The main adapter used for all recyclerView
 *
 * @param dataset The list of Images
 */
class MainAdapter(private val dataset: ArrayList<Image>) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.frame_imageview, parent, false) as FrameLayout
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (dataset[position].is_album) {
            Glide.with(holder.view).load(dataset[position].images?.get(0)?.link).into(holder.view.imageView)
        } else {
            Glide.with(holder.view).load(dataset[position].link).into(holder.view.imageView)
        }
        holder.view.imageView.setOnClickListener {
            val intent = Intent(holder.view.context, ImageActivity::class.java)
            intent.putExtra("LINK", if (dataset[position].is_album) { dataset[position].images?.get(0)?.link } else { dataset[position].link })
            holder.view.context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataset.size
}