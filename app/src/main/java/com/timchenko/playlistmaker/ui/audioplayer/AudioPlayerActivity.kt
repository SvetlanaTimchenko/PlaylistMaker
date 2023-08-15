package com.timchenko.playlistmaker.ui.audioplayer

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.timchenko.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.timchenko.playlistmaker.presentation.models.TrackDetails
import java.io.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private val viewModel: AudioPlayerViewModel by viewModel()
    private lateinit var trackDetails: TrackDetails

    private lateinit var savedTimeTrack: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackDetails = getSerializable("track", TrackDetails::class.java)

        viewModel.observeStateLiveData().observe(this) {
            render(it)
        }
        viewModel.observeTimeLiveData().observe(this) {
            savedTimeTrack = it
        }

        binding.artist.text = trackDetails.artistName
        binding.track.text = trackDetails.trackName
        binding.countryVar.text = trackDetails.country
        binding.genderVar.text = trackDetails.primaryGenreName
        binding.timeVar.text = trackDetails.trackTime
        binding.yearVar.text = trackDetails.releaseYear

        binding.albumGroup.visibility = View.GONE

        trackDetails.collectionName?.let {
            binding.albumVar.text = trackDetails.collectionName
            binding.albumGroup.visibility = View.VISIBLE
        }

        // обложка
        Glide
            .with(this)
            .load(trackDetails.getCoverArtwork())
            .placeholder(R.drawable.placeholderph)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelOffset(R.dimen.value_8)))
            .into(this.findViewById(R.id.trackCover))

        // готовим медиаплеер
        viewModel.preparePlayer(trackDetails.previewUrl)

        binding.playBtn.setOnClickListener {
            viewModel.playbackControl()
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

    private fun render(state: PlayerState) {
        when (state) {
            PlayerState.PLAYING -> {
                binding.playBtn.setImageResource(R.drawable.buttonpause)
                binding.timeBar.text = savedTimeTrack
            }
            PlayerState.PAUSED -> {
                binding.playBtn.setImageResource(R.drawable.buttonplay)
            }
            PlayerState.PREPARED, PlayerState.DEFAULT -> {
                binding.playBtn.setImageResource(R.drawable.buttonplay)
                binding.timeBar.text = getString(R.string.timebar_start)
                savedTimeTrack = getString(R.string.timebar_start)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(PLAY_TIME, binding.timeBar.text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedTimeTrack = savedInstanceState.getCharSequence(PLAY_TIME).toString()

    }

    private fun <T : Serializable?> getSerializable(name: String, clazz: Class<T>): T
    {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(name, clazz)!!
        else
            intent.getSerializableExtra(name) as T
    }

    companion object {
        const val PLAY_TIME = "PLAY_TIME"
    }
}