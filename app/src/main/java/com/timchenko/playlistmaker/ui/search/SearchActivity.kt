package com.timchenko.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.databinding.ActivitySearchBinding
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.presentation.mapper.TrackMapper
import com.timchenko.playlistmaker.ui.audioplayer.AudioPlayerActivity
import com.timchenko.playlistmaker.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModel()

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchResultsAdapter: TrackAdapter

    private lateinit var previousRequest: String

    private var simpleTextWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeHistoryState().observe(this) {
            showSearchHistory(it)
        }

        trackAdapter = TrackAdapter(setAdapterListener())
        searchResultsAdapter = TrackAdapter(setAdapterListener())

        binding.recyclerTracks.adapter = trackAdapter

        // реализация клика на кнопку Назад
        binding.back.setOnClickListener {
            this.finish()
        }


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

    override fun onDestroy() {
        super.onDestroy()
        simpleTextWatcher?.let { binding.searchEditText.removeTextChangedListener(it) }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_EDITTEXT, binding.searchEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.searchEditText.setText(savedInstanceState.getString(SEARCH_EDITTEXT))
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun setAdapterListener(): TrackAdapter.Listener {
        return object : TrackAdapter.Listener {
            override fun onClick(track: Track) {
                // добавляем в историю поиска
                viewModel.onClick(track)

                // открываем аудиоплеер
                val displayIntent = Intent(this@SearchActivity, AudioPlayerActivity::class.java)
                displayIntent.putExtra("track", TrackMapper.map(track))
                startActivity(displayIntent)
            }
        }
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

    companion object {
        const val SEARCH_EDITTEXT = "SEARCH_EDITTEXT"
    }
}