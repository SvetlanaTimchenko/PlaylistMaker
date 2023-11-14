package com.timchenko.playlistmaker.ui.audioplayer

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.domain.models.Playlist

class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val playlistName: TextView = itemView.findViewById(R.id.playlistAddName)
    private val playlistTrackCounter: TextView = itemView.findViewById(R.id.playlistAddTrackCounter)
    private val cover: ImageView = itemView.findViewById(R.id.playlistAddCover)

    fun bind(item: Playlist) {
        playlistName.text = item.name
        cover.contentDescription = item.name
        playlistTrackCounter.text = itemView.resources.getQuantityString(R.plurals.track_counter, item.tracks.size, item.tracks.size)

        Glide.with(itemView)
            .load(item.uri)
            .placeholder(R.drawable.placeholderph)
            .transform(CenterCrop(), RoundedCorners(itemView.context.resources.getDimensionPixelOffset(R.dimen.value_8)))
            .into(itemView.findViewById(R.id.playlistAddCover))
    }
}