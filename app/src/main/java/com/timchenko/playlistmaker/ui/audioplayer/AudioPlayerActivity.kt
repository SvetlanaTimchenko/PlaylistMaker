package com.timchenko.playlistmaker.ui.audioplayer

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.timchenko.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.timchenko.playlistmaker.presentation.models.TrackDetails
import java.io.Serializable


class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var binding: ActivityAudioPlayerBinding

    private lateinit var trackDetails: TrackDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackDetails = getSerializable("track", TrackDetails::class.java)

        viewModel = ViewModelProvider(this) [AudioPlayerViewModel::class.java]
        viewModel.observePlayButtonState().observe(this) {
            updatePlayButton(it)
        }
        viewModel.observeSecondsState().observe(this) {
            updateTimer(it)
        }
        viewModel.observePlayButtonEnabledState().observe(this) {
            enablePlayButton(it)
        }

        binding.artist.text = trackDetails.artistName
        binding.track.text = trackDetails.trackName
        binding.countryVar.text = trackDetails.country
        binding.genderVar.text = trackDetails.primaryGenreName
        binding.timeVar.text = trackDetails.trackTime
        binding.yearVar.text = trackDetails.releaseYear

        if (trackDetails.collectionName.isNotEmpty()) {
            binding.albumVar.text = trackDetails.collectionName
            binding.albumGroup.visibility = View.VISIBLE
        }
        else {
            binding.albumGroup.visibility = View.GONE
        }

        // обложка
        Glide
            .with(this)
            .load(trackDetails.getCoverArtwork())
            .placeholder(R.drawable.placeholderph)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelOffset(R.dimen.value_8)))
            .into(this.findViewById(R.id.trackCover))

        enablePlayButton(enabled = false)

        // готовим медиаплеер
        viewModel.preparePlayer(previewUrl = trackDetails.previewUrl)

        // проигрыватель
        binding.timeBar.text = "00:30" // устанавливаем таймер для всех отрывков в 30 секунд
        binding.playBtn.setOnClickListener {
            playbackControl()
        }

        // реализация клика на кнопку Назад
        binding.back.setOnClickListener {
            this.finish()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun enablePlayButton(enabled: Boolean) {
        binding.playBtn.isEnabled = enabled
    }

    private fun updatePlayButton(isPaused: Boolean) {
        if (isPaused) {
            binding.playBtn.setImageResource(R.drawable.buttonpause)
        }
        else {
            binding.playBtn.setImageResource(R.drawable.buttonplay)
        }
    }

    private fun updateTimer(seconds: Long) {
        binding.timeBar.text = String.format("%02d:%02d", seconds / 60, seconds % 60)
    }

    private fun playbackControl() {
        val secondsCount = binding.timeBar.text
            .toString()
            .replace(":", "")
            .toLong()
        viewModel.playbackControl(secondsCount = secondsCount)
    }

    private fun <T : Serializable?> getSerializable(name: String, clazz: Class<T>): T
    {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(name, clazz)!!
        else
            intent.getSerializableExtra(name) as T
    }
}