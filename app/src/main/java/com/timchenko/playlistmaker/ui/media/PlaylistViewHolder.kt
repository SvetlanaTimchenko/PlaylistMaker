package com.timchenko.playlistmaker.ui.media

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
    private val playlistName: TextView = itemView.findViewById(R.id.playlistName)
    private val playlistTrackCounter: TextView = itemView.findViewById(R.id.playlistTrackCounter)
    private val cover: ImageView = itemView.findViewById(R.id.playlistCover)

    fun bind(item: Playlist) {
        playlistName.text = item.name
        cover.contentDescription = item.name
        playlistTrackCounter.text = item.tracksCounter.let {
            itemView.resources.getQuantityString(
                R.plurals.track_counter,
                it,
                item.tracksCounter
            )
        }

        Glide.with(itemView)
            .load(item.uri)
            .placeholder(R.drawable.placeholderph)
            .transform(CenterCrop(), RoundedCorners(itemView.context.resources.getDimensionPixelOffset(R.dimen.value_8)))
            .into(itemView.findViewById(R.id.playlistCover))
    }
}