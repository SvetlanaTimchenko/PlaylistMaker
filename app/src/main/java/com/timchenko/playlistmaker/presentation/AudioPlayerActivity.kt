package com.timchenko.playlistmaker.presentation

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.timchenko.playlistmaker.Creator
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.domain.AudioPlayerInteractor
import com.timchenko.playlistmaker.domain.models.State
import com.timchenko.playlistmaker.domain.models.Track
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale


class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val DELAY_MS = 1000L
    }

    private lateinit var track: Track
    private lateinit var country : TextView
    private lateinit var gender : TextView
    private lateinit var year : TextView
    private lateinit var time : TextView
    private lateinit var artist : TextView
    private lateinit var trackName : TextView

    private lateinit var albumGroup : Group
    private lateinit var album : TextView

    private lateinit var play: ImageButton
    private lateinit var timeBar: TextView
    private val audioPlayerInteractor : AudioPlayerInteractor =
        Creator.provideAudioPlayerInteractor()
    private var playIconId: Int = 0
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var playerRunnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        // проигрыватель
        play = findViewById(R.id.playBtn)
        timeBar = findViewById(R.id.timeBar)
        timeBar.text = "00:30" // устанавливаем таймер для всех отрывков в 30 секунд
        play.setOnClickListener {
            playbackControl()
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

        play.isEnabled = false

        // медиаплеер
        audioPlayerInteractor.preparePlayer(track.previewUrl) { state ->
            when (state) {
                State.PREPARED -> {
                    play.isEnabled = true
                }
                else -> {}
            }
        }

        // реализация клика на кнопку Назад
        val buttonBack = findViewById<ImageView>(R.id.back)
        buttonBack.setOnClickListener {
            this.finish()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
//        mediaPlayer.release()
        audioPlayerInteractor.shutDownPlayer()
        handler.removeCallbacks(playerRunnable)
    }

    private fun playbackControl() {
        val secondsCount = timeBar.text
            .toString()
            .replace(":", "")
            .toLong()

        if (secondsCount > 0) {
            audioPlayerInteractor.switchPlayer { state ->
                when (state) {
                    State.PAUSED, State.PREPARED -> {
                        playIconId = resources.getIdentifier("buttonplay", "drawable", packageName)
                        play.setImageResource(playIconId)
                        handler.removeCallbacks(playerRunnable)
                    }
                    State.PLAYING -> {
                        playIconId = resources.getIdentifier("buttonpause", "drawable", packageName)
                        play.setImageResource(playIconId)
                        startTimer(secondsCount)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun startTimer(duration: Long) {
        val startTime = System.currentTimeMillis()
        playerRunnable = createUpdateTimerTask(startTime, duration * DELAY_MS)
        handler.post(playerRunnable)
    }

    private fun createUpdateTimerTask(startTime: Long, duration: Long): Runnable {
        return object : Runnable {
            override fun run() {
                // Сколько прошло времени с момента запуска таймера
                val elapsedTime = System.currentTimeMillis() - startTime
                // Сколько осталось до конца
                val remainingTime = duration - elapsedTime

                if (remainingTime > 0) {
                    // Если всё ещё отсчитываем секунды —
                    // обновляем UI и снова планируем задачу
                    val seconds = remainingTime / DELAY_MS
                    timeBar.text = String.format("%02d:%02d", seconds / 60, seconds % 60)
                    handler.postDelayed(this, DELAY_MS)
                } else {
                    pausePlayer()
                }
            }
        }
    }

    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        playIconId = resources.getIdentifier("buttonplay", "drawable", packageName)
        play.setImageResource(playIconId)
        handler.removeCallbacks(playerRunnable)
    }

    private fun <T : Serializable?> getSerializable(name: String, clazz: Class<T>): T
    {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(name, clazz)!!
        else
            intent.getSerializableExtra(name) as T
    }
}