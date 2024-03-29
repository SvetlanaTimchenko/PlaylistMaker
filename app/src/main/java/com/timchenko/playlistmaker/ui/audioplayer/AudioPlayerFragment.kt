package com.timchenko.playlistmaker.ui.audioplayer

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.timchenko.playlistmaker.domain.models.Playlist
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.timchenko.playlistmaker.presentation.models.PlaylistState
import com.timchenko.playlistmaker.presentation.models.PlaylistTrackState
import com.timchenko.playlistmaker.util.Formatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerFragment : Fragment() {
    private lateinit var binding: FragmentAudioPlayerBinding
    private val viewModel: AudioPlayerViewModel by viewModel()

    private lateinit var track: Track

    private var savedTimeTrack: String? = null

    private val playlistAdapter = PlaylistAdapter {
        addTrackInPlaylist(it)
    }
    private var isPlaylistClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(TRACK_INFO, Track::class.java)!!
        } else {
            arguments?.getParcelable(TRACK_INFO)!!
        }

        viewModel.observePlayerState().observe(viewLifecycleOwner) {
            this.savedTimeTrack = it.progress
            binding.playBtn.isEnabled = it.isPlayButtonEnabled
            binding.playBtn.setImageResource(it.buttonResource)
            binding.timeBar.text = it.progress
        }

        viewModel.observeFavoriteState().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.LikeBtn.setImageResource(R.drawable.buttonlike)
            } else {
                binding.LikeBtn.setImageResource(R.drawable.buttonliken)
            }
            track.isFavorite = isFavorite
        }

        if (track.isFavorite) binding.LikeBtn.setImageResource(R.drawable.buttonlike)

        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            renderPlaylists(it)
        }
        binding.recyclerPlaylistsInPLayer.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPlaylistsInPLayer.adapter = playlistAdapter

        val bottomSheetContainer = binding.bottomSheetPlaylist
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.visibility = View.GONE
                    else -> binding.overlay.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })

        viewModel.observeAddedToPlaylistState().observe(viewLifecycleOwner) { addedToPlaylist ->
            when (addedToPlaylist) {
                is PlaylistTrackState.Match -> renderToast(addedToPlaylist.playlistName, false)
                is PlaylistTrackState.Added -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    renderToast(addedToPlaylist.playlistName, true)
                }

                else -> Unit
            }

        }

        binding.artist.text = track.artistName
        binding.track.text = track.trackName
        binding.countryVar.text = track.country
        binding.genderVar.text = track.primaryGenreName
        binding.timeVar.text = Formatter.convertMillisToMinutesAndSeconds(track.trackTimeMillis)
        binding.yearVar.text = convertToYear(track.releaseDate)
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
            .into(binding.trackCover)

        viewModel.preparePlayer(track.previewUrl)

        binding.playBtn.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.LikeBtn.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        binding.addToPlayListBtn.setOnClickListener {
            viewModel.getPlaylists()
            binding.overlay.visibility = View.VISIBLE
            bottomSheetBehavior.halfExpandedRatio = 0.65F
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_addPlaylistFragment)

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.playerMainScreen.isVisible = false
            binding.overlay.visibility = View.GONE
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    private fun renderToast(playlistName: String?, added: Boolean) {
        if (added) {

            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_track_added, playlistName),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_track_exists, playlistName),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun renderPlaylists(state: PlaylistState) {
        when (state) {
            is PlaylistState.Empty -> showEmpty()
            is PlaylistState.Content -> showContent(state.playlists)
        }
    }

    private fun showEmpty() {
        binding.recyclerPlaylistsInPLayer.visibility = View.GONE
    }

    private fun showContent(playlists: List<Playlist>) {
        playlistAdapter.playlists.clear()
        playlistAdapter.playlists.addAll(playlists)
        playlistAdapter.notifyDataSetChanged()

        binding.recyclerPlaylistsInPLayer.visibility = View.VISIBLE
    }

    private fun clickDebounce(): Boolean {
        val current = isPlaylistClickAllowed
        if (isPlaylistClickAllowed) {
            isPlaylistClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isPlaylistClickAllowed = true
            }
        }
        return current
    }

    private fun addTrackInPlaylist(playlist: Playlist) {
        if (clickDebounce()) {
            viewModel.addTrackInPlaylist(playlist, track)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(PLAY_TIME, binding.timeBar.text)
    }

    private fun convertToYear(releaseDate: String?): String? {
        return Formatter.convertToYear(releaseDate)
    }

    companion object {
        const val PLAY_TIME = "PLAY_TIME"
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val TRACK_INFO = "track"
    }
}