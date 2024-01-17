package com.example.wallpaperapp.Presentation.adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wallpaperapp.R
import com.example.wallpaperapp.domain.entity.WallpaperLink


class ImagesRecyclerViewAdapter(private var dataSet: List<WallpaperLink>,private val onWallpaperItemClick: (String) -> Unit) :
    RecyclerView.Adapter<ImagesRecyclerViewAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView:AppCompatImageView

        init {
            // Define click listener for the ViewHolder's View
            imageView = view.findViewById(R.id.imageView)
        }
    }
    fun setData(newData: List<WallpaperLink>) {
        dataSet = newData
        notifyDataSetChanged() // Notify the RecyclerView that the data has changed
    }


    /*constructor(parcel: Parcel) : this(parcel.createStringArray()!!) {
    }*/

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.text_row_item, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
       // viewHolder.imageView. = dataSet[position]
        Glide.with(viewHolder.imageView.context)
            .load(dataSet[position].wallpaperLink)
            .into(viewHolder.imageView)
        viewHolder.imageView.setOnClickListener{
            //onClickListener.onClickImage(dataSet[position].wallpaperLink)
            onWallpaperItemClick(dataSet[position].wallpaperLink)

        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    }
interface ItemOnClickListener{
    fun onClickImage(wallpaperLink :String)
}


