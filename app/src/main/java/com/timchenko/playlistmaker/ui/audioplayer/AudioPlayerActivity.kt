package com.timchenko.playlistmaker.ui.audioplayer

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import java.io.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding
    private val viewModel: AudioPlayerViewModel by viewModel()
    private lateinit var track: Track

    private lateinit var savedTimeTrack: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = getSerializable("track", Track::class.java)

        viewModel.observePlayerState().observe(this) {
            savedTimeTrack = it.progress
            binding.playBtn.isEnabled = it.isPlayButtonEnabled
            binding.playBtn.setImageResource(it.buttonResource)
            binding.timeBar.text = it.progress
        }

        viewModel.observeFavoriteState().observe(this) { isFavorite ->
            if (isFavorite) {
                binding.LikeBtn.setImageResource(R.drawable.buttonlike)
            }
            else {
                binding.LikeBtn.setImageResource(R.drawable.buttonliken)
            }
            track.isFavorite = !isFavorite
        }

        binding.artist.text = track.artistName
        binding.track.text = track.trackName
        binding.countryVar.text = track.country
        binding.genderVar.text = track.primaryGenreName
        binding.timeVar.text = track.trackTime
        binding.yearVar.text = convertToYear(track.releaseDate)

        if (track.isFavorite) binding.LikeBtn.setImageResource(R.drawable.buttonlike)

        binding.albumGroup.visibility = View.GONE

        track.collectionName?.let {
            binding.albumVar.text = track.collectionName
            binding.albumGroup.visibility = View.VISIBLE
        }

        // обложка
        Glide
            .with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholderph)
            .centerCrop()
            .transform(RoundedCorners(resources.getDimensionPixelOffset(R.dimen.value_8)))
            .into(this.findViewById(R.id.trackCover))

        // готовим медиаплеер
        viewModel.preparePlayer(track.previewUrl)

        binding.playBtn.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.LikeBtn.setOnClickListener {
            viewModel.onFavoriteClicked(track)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(PLAY_TIME, binding.timeBar.text)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedTimeTrack = savedInstanceState.getCharSequence(PLAY_TIME).toString()

    }

    private fun convertToYear(releaseDate: String?): String? {
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(Date.from(Instant.parse(releaseDate)))
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