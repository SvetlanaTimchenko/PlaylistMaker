package com.timchenko.playlistmaker

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.presentation.AudioPlayerActivity

// берем тот же RV, что и для отображения списка найденных треков
// и модифицируем под работу со списком из 10 последних треков, которые просматривали
class SearchResultsAdapter() : RecyclerView.Adapter<TrackViewHolder> () {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    var tracks = ArrayList<Track>()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

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
            if (clickDebounce()) {
                val displayIntent = Intent(it.context, AudioPlayerActivity::class.java)
                displayIntent.putExtra("track", tracks[position])
                it.context.startActivity(displayIntent)
            }
        }


    }

    private fun clickDebounce() : Boolean {
        val  current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}

