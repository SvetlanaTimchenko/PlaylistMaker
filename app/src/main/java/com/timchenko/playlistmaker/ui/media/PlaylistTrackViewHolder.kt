package com.timchenko.playlistmaker.ui.media

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.util.Formatter

class PlaylistTrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.playlistAddName)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameInList)
    private val trackTime: TextView = itemView.findViewById(R.id.timeInList)
    private val cover: ImageView = itemView.findViewById(R.id.trackCover)

    fun bind(item: Track) {
        trackName.text = item.trackName
        cover.contentDescription = item.artistName + " : " + item.trackName
        artistName.text = item.artistName
        trackTime.text = Formatter.convertMillisToMinutesAndSeconds(item.trackTimeMillis)

        Glide
            .with(itemView)
            .load(item.getCoverArtworkForPlaylist())
            .placeholder(R.drawable.placeholderph)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(itemView.findViewById(R.id.trackCover))
    }
}