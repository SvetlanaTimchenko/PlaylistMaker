package com.timchenko.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackAdapter (sharedPreferences: SharedPreferences) : RecyclerView.Adapter<TrackViewHolder> () {

    var tracks = ArrayList<Track>()
    var searchHistory = SearchHistory(sharedPreferences)

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
            searchHistory.addTrack(track)

            // открываем аудиоплеер
            val displayIntent = Intent(it.context, AudioPlayerActivity::class.java)
            displayIntent.putExtra("track", Gson().toJson(track))
            it.context.startActivity(displayIntent)
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}