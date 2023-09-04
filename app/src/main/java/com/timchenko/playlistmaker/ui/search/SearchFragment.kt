package com.timchenko.playlistmaker.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.databinding.FragmentSearchBinding
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.presentation.mapper.TrackMapper
import com.timchenko.playlistmaker.presentation.search.SearchViewModel
import com.timchenko.playlistmaker.ui.audioplayer.AudioPlayerActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModel()

    private var isClickAllowed = true

    private val trackAdapter = TrackAdapter {
        if (clickDebounce()) {
            switchToPlayer(it)
        }
    }

    private val searchResultsAdapter = TrackAdapter {
        if (clickDebounce()) {
            switchToPlayer(it)
        }
    }

    private lateinit var previousRequest: String

    private var simpleTextWatcher: TextWatcher? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeHistoryState().observe(viewLifecycleOwner) {
            showSearchHistory(it)
        }

        binding.recyclerTracks.adapter = trackAdapter

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.searchClear.visibility = View.VISIBLE
                }
                previousRequest = s?.toString() ?: ""
                viewModel.searchDebounce(changedText = previousRequest)

                binding.searchPrefs.visibility = if (binding.searchEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        simpleTextWatcher.let { binding.searchEditText.addTextChangedListener(it) }

        binding.searchClear.setOnClickListener {
            binding.searchEditText.setText("")
            binding.searchClear.visibility = View.GONE
            binding.searchEditText.clearFocus()
            clearContent()
        }

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchEditText.text.isNotEmpty()) {
                    previousRequest = binding.searchEditText.text.toString()
                    viewModel.searchDebounce(binding.searchEditText.text.toString())
                }
            }
            false
        }

        binding.searchEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.getSearchHistory()
            binding.searchPrefs.visibility =
                if (hasFocus && binding.searchEditText.text.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.refreshSearch.setOnClickListener {
            binding.searchEditText.setText(previousRequest)
            viewModel.searchDebounce(previousRequest)
        }

        binding.clearSearchHistory.setOnClickListener {
            viewModel.clearHistory()
            binding.searchPrefs.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        simpleTextWatcher?.let { binding.searchEditText.removeTextChangedListener(it) }
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Error -> showError()
            is TracksState.Empty -> showEmpty()
            is TracksState.Content -> showContent(state.tracks)
        }
    }
    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.recyclerTracks.visibility = View.GONE
        binding.searchPrefs.visibility = View.GONE
    }

    private fun showError() {
        binding.errorSearchImage.setImageResource(R.drawable.il_search_connect)
        binding.errorSearchText.setText(R.string.no_connetion)
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.recyclerTracks.visibility = View.GONE
        binding.searchPrefs.visibility = View.GONE

        binding.refreshSearch.visibility = View.VISIBLE
    }


    private fun showEmpty() {
        binding.errorSearchImage.setImageResource(R.drawable.il_search_error)
        binding.errorSearchText.setText(R.string.nothing_found)
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.recyclerTracks.visibility = View.GONE
        binding.searchPrefs.visibility = View.GONE

        binding.refreshSearch.visibility = View.GONE
    }

    private fun showContent(newTracksList: List<Track>) {
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(newTracksList)
        trackAdapter.notifyDataSetChanged()

        binding.recyclerTracks.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.searchPrefs.visibility = View.GONE
    }

    private fun clearContent() {
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun showSearchHistory(tracks: ArrayList<Track>) {
        if (tracks.isEmpty()) {
            binding.searchPrefs.visibility = View.GONE
        }
        else {
            searchResultsAdapter.tracks = tracks
            binding.recyclerSearch.adapter = searchResultsAdapter
            searchResultsAdapter.notifyDataSetChanged()

            binding.recyclerSearch.visibility = View.VISIBLE
            binding.recyclerTracks.visibility = View.GONE
            binding.progressBar.visibility = View.GONE
            binding.placeholderMessage.visibility = View.GONE
            binding.searchPrefs.visibility = View.VISIBLE
        }

    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun switchToPlayer(track: Track) {
        viewModel.onClick(track)

        val displayIntent = Intent(requireContext(), AudioPlayerActivity::class.java)
        displayIntent.putExtra("track", TrackMapper.map(track))
        startActivity(displayIntent)
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}