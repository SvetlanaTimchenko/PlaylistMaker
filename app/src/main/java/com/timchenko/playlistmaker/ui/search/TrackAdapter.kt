package com.timchenko.playlistmaker.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.domain.models.Track

class TrackAdapter (private val listener: Listener) : RecyclerView.Adapter<TrackViewHolder> () {

    var tracks = ArrayList<Track>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            listener.onClick(track = track)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun interface Listener {
        fun onClick(track: Track)
    }

}