package com.timchenko.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        // реализация клика на кнопку Назад
        val buttonBack = findViewById<ImageView>(R.id.back)
        buttonBack.setOnClickListener {
            this.finish()
        }

        // достаем трек
        val trackString = intent.getStringExtra("track")
        track = Gson().fromJson(trackString, Track::class.java)

        // обновляем xml
        val country = findViewById<TextView>(R.id.countryVar)
        val gender = findViewById<TextView>(R.id.genderVar)
        val year = findViewById<TextView>(R.id.yearVar)
        val time = findViewById<TextView>(R.id.timeVar)
        val artist = findViewById<TextView>(R.id.artist)
        val trackName = findViewById<TextView>(R.id.track)

        val albumGroup = findViewById<Group>(R.id.albumGroup)
        val album = findViewById<TextView>(R.id.albumVar)
        artist.text = track.artistName
        trackName.text = track.trackName
        country.text = track.country
        gender.text = track.primaryGenreName
        time.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)

        val releaseDate = Date.from(Instant.parse(track.releaseDate))
        year.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(releaseDate)

        if (track.collectionName.isNotEmpty()) {
            album.text = track.collectionName
            albumGroup.visibility = View.VISIBLE
        }
        else {
            albumGroup.visibility = View.GONE
        }

        // обложка
        Glide
            .with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholderph)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelOffset(R.dimen.value_8)))
            .into(this.findViewById(R.id.trackCover))

    }
}