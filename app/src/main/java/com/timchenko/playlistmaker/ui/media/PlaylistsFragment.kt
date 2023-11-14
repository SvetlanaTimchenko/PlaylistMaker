package com.timchenko.playlistmaker.ui.media

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.databinding.FragmentPlaylistsBinding
import com.timchenko.playlistmaker.domain.models.Playlist
import com.timchenko.playlistmaker.presentation.media.PlaylistsFragmentViewModel
import com.timchenko.playlistmaker.presentation.models.PlaylistState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel: PlaylistsFragmentViewModel by viewModel()

    private val playlistAdapter = PlaylistAdapter {
        switchToPlaylist(it)
    }
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.recyclerPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerPlaylists.adapter = playlistAdapter

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_addPlaylistFragment)
        }
    }

    private fun render(state: PlaylistState) {
        when(state) {
            is PlaylistState.Empty -> showEmpty()
            is PlaylistState.Content -> showContent(state.playlists)
        }
    }

    private fun showEmpty() {
        binding.recyclerPlaylists.visibility = View.GONE
        binding.emptyMediaPlaylists.visibility = View.VISIBLE
        binding.emptyMediaPlaylistsText.visibility = View.VISIBLE
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.emptyMediaPlaylists.visibility = View.GONE
        binding.emptyMediaPlaylistsText.visibility = View.GONE

        playlistAdapter.playlists.clear()
        playlistAdapter.playlists.addAll(playlists)
        playlistAdapter.notifyDataSetChanged()

        binding.recyclerPlaylists.visibility = View.VISIBLE
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

    private fun switchToPlaylist(playlist: Playlist) {
        if (clickDebounce()) {
            findNavController().navigate(
                R.id.action_mediaFragment_to_playlistDetailsFragment,
                PlaylistDetailsFragment.createArgs(playlist.id)
                )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylists()
        isClickAllowed = true
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        fun newInstance() : PlaylistsFragment {
            return PlaylistsFragment()
        }
    }
}