package com.timchenko.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

// берем тот же RV, что и для отображения списка найденных треков
// и модифицируем под работу со списком из 10 последних треков, которые просматривали
class SearchResultsAdapter() : RecyclerView.Adapter<TrackViewHolder> () {

    var tracks = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])

        holder.itemView.setOnClickListener {
            // открываем аудиоплеер
            val displayIntent = Intent(it.context, AudioPlayerActivity::class.java)
            displayIntent.putExtra("track", Gson().toJson(tracks[position]))
            it.context.startActivity(displayIntent)
        }


    }
}
