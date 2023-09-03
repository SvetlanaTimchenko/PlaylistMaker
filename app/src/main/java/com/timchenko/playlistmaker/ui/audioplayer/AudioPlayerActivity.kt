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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackDetails = getSerializable("track", TrackDetails::class.java)

        viewModel.observePlayerState().observe(this) {
            binding.playBtn.isEnabled = it.isPlayButtonEnabled
            binding.playBtn.setImageResource(it.buttonResource)
            binding.timeBar.text = it.progress
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
        viewModel.onPause()
    }

    private fun <T : Serializable?> getSerializable(name: String, clazz: Class<T>): T
    {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(name, clazz)!!
        else
            intent.getSerializableExtra(name) as T
    }
}