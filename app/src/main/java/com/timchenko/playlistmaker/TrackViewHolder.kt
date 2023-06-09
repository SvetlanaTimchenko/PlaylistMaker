package com.timchenko.playlistmaker

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.timchenko.playlistmaker.domain.models.Track
import java.util.Locale

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val description: TextView = itemView.findViewById(R.id.description)
    private val cover: ImageView = itemView.findViewById(R.id.trackCover)

    fun bind(item: Track) {
        trackName.text = item.trackName
        cover.contentDescription = item.artistName + " : " + item.trackName
        description.text = item.artistName + " \u2022 " + SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTime)

        Glide
            .with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.placeholderph)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(itemView.findViewById(R.id.trackCover))
    }
}