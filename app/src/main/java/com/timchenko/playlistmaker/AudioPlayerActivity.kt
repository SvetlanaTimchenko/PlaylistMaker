package com.timchenko.playlistmaker

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var track: Track
    private lateinit var country : TextView
    private lateinit var gender : TextView
    private lateinit var year : TextView
    private lateinit var time : TextView
    private lateinit var artist : TextView
    private lateinit var trackName : TextView

    private lateinit var albumGroup : Group
    private lateinit var album : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        // реализация клика на кнопку Назад
        val buttonBack = findViewById<ImageView>(R.id.back)
        buttonBack.setOnClickListener {
            this.finish()
        }

        // достаем трек
        track = getSerializable("track", Track::class.java)

        // обновляем xml
        country = findViewById(R.id.countryVar)
        gender = findViewById(R.id.genderVar)
        year = findViewById(R.id.yearVar)
        time = findViewById(R.id.timeVar)
        artist = findViewById(R.id.artist)
        trackName = findViewById(R.id.track)

        albumGroup = findViewById(R.id.albumGroup)
        album = findViewById(R.id.albumVar)

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

    private fun <T : Serializable?> getSerializable(name: String, clazz: Class<T>): T
    {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(name, clazz)!!
        else
            intent.getSerializableExtra(name) as T
    }
}