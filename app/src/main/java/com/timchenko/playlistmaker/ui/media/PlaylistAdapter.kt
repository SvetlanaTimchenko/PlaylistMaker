package com.timchenko.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.domain.models.Playlist

class PlaylistAdapter(private val listener: Listener) : RecyclerView.Adapter<PlaylistViewHolder> () {

    var playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlist_item, parent, false)
        return PlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)

        holder.itemView.setOnClickListener {
            listener.onClick(playlist)
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    fun interface Listener {
        fun onClick(playlist: Playlist)
    }
}