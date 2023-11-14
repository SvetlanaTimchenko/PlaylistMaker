package com.timchenko.playlistmaker.ui.media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.databinding.FragmentFavoritesBinding
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.presentation.media.FavoritesFragmentViewModel
import com.timchenko.playlistmaker.presentation.models.FavoriteState
import com.timchenko.playlistmaker.ui.search.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoritesFragmentViewModel by viewModel()

    private var isClickAllowed = true
    private val trackAdapter = TrackAdapter {
        switchToPlayer(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.recyclerFavorites.adapter = trackAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTracks()
        isClickAllowed = true
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Empty -> showEmpty()
            is FavoriteState.Content -> showContent(state.tracks)
        }
    }

    private fun showEmpty() {
        binding.emptyMedia.visibility = View.VISIBLE
        binding.recyclerFavorites.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>) {
        binding.emptyMedia.visibility = View.GONE

        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()

        binding.recyclerFavorites.visibility = View.VISIBLE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun switchToPlayer(track: Track) {
        if (clickDebounce()) {
            val bundle = Bundle()
            bundle.putParcelable(TRACK_INFO, track)

            findNavController().navigate(
                R.id.actionGlobalPlayer,
                bundle
            )
        }
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private const val TRACK_INFO = "track"
        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}