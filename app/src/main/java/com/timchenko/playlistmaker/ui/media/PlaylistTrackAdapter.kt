package com.timchenko.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.domain.models.Track

class PlaylistTrackAdapter (private val listener: Listener) : RecyclerView.Adapter<PlaylistTrackViewHolder> () {

    var tracks = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return PlaylistTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            listener.onShortClick(track = track)
        }
        holder.itemView.setOnLongClickListener {
            listener.onLongClick(track.trackId)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    interface Listener {
        fun onShortClick(track: Track)
        fun onLongClick(trackId: Int): Boolean
    }

}