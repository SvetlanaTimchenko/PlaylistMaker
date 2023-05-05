package com.timchenko.playlistmaker

import android.content.Context
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private val iTunesUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create<ITunesApi>()
    private val tracks = ArrayList<Track>()
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var placeholderMessage: LinearLayout
    private lateinit var placeholderImage: ImageView
    private lateinit var placeholderText: TextView
    private lateinit var placeholderButton: Button
    private lateinit var inputEditText: EditText
    private lateinit var rvTracks: RecyclerView
    private var errorIconId: Int = 0
    private var errorTextId: Int = 0
    private lateinit var previousRequest: String

    private val searchResultsAdapter = SearchResultsAdapter()
    private lateinit var searchResults: LinearLayout
    private lateinit var listener: OnSharedPreferenceChangeListener
    private lateinit var searchHistory: SearchHistory


    companion object {
        const val SEARCH_EDITTEXT = "SEARCH_EDITTEXT"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)

        trackAdapter = TrackAdapter(sharedPreferences)
        searchHistory = SearchHistory(sharedPreferences)

        // реализация клика на кнопку Назад
        val buttonBack = findViewById<ImageView>(R.id.back)
        buttonBack.setOnClickListener {
            this.finish()
        }

        inputEditText = findViewById<EditText>(R.id.searchEditText)
        val clearSearchButton = findViewById<ImageView>(R.id.searchClear)

        trackAdapter.tracks = tracks
        rvTracks = findViewById<RecyclerView>(R.id.recyclerTracks)
        rvTracks.adapter = trackAdapter

        clearSearchButton.setOnClickListener {
            inputEditText.setText("")
            clearSearchButton.visibility = View.GONE
            inputEditText.clearFocus()
            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            showMessage()
        }

        searchResults = findViewById(R.id.searchPrefs)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    clearSearchButton.visibility = View.VISIBLE
                }
                searchResults.visibility = if (inputEditText.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            val sp = searchHistory.getFromHistory()
            if (sp.isNotEmpty()) {
                searchResults.visibility =
                    if (hasFocus && inputEditText.text.isEmpty()) View.VISIBLE else View.GONE
                showSearchHistory(searchHistory)
            }
            else {
                searchResults.visibility = View.GONE
            }
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (inputEditText.text.isNotEmpty()) {
                    previousRequest = inputEditText.text.toString()
                }
                makeSearch(previousRequest)
            }
            false
        }

        val refreshSearchButton = findViewById<Button>(R.id.refreshSearch)
        refreshSearchButton.setOnClickListener {
            inputEditText.setText(previousRequest)
            makeSearch(previousRequest)
        }

        val clearSearchResultsButton = findViewById<Button>(R.id.clearSearchHistory)
        clearSearchResultsButton.setOnClickListener {
            searchHistory.clearHistory()
            searchResults.visibility = View.GONE
        }

        listener = OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == SEARCH_HISTORY) {
                showSearchHistory(searchHistory)
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

    }

    private fun showSearchHistory(searchHistory: SearchHistory) {

        val rvTracks = findViewById<RecyclerView>(R.id.recyclerSearch)

        searchResultsAdapter.tracks = searchHistory.getFromHistory()
        rvTracks.adapter = searchResultsAdapter
        searchResultsAdapter.notifyDataSetChanged()

        rvTracks.visibility = View.VISIBLE
    }

    private fun makeSearch(text: String) {
        // убираем плейсхолдер, если он был показан
        showMessage()
        if (text.isNotEmpty()) {
            iTunesService.search(text).enqueue(object : Callback<ITunesResponse> {
                override fun onResponse(
                    call: Call<ITunesResponse>,
                    response: Response<ITunesResponse>
                ) {
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.resultCount!! > 0) {
                            tracks.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                            rvTracks.visibility = View.VISIBLE
                        }
                        else {
                            errorIconId = resources.getIdentifier(
                                "il_search_error",
                                "drawable",
                                packageName
                            )
                            errorTextId = resources.getIdentifier(
                                "nothing_found",
                                "string",
                                packageName
                            )
                            showMessage(textId = errorTextId, imageId = errorIconId)
                        }
                    }
                    else {
                        errorIconId = resources.getIdentifier(
                            "il_search_connect",
                            "drawable",
                            packageName
                        )
                        errorTextId = resources.getIdentifier(
                            "no_connetion",
                            "string",
                            packageName
                        )
                        showMessage(textId = errorTextId, imageId = errorIconId, showButton = true)
                    }
                }

                override fun onFailure(call: Call<ITunesResponse>, t: Throwable) {
                    errorIconId = resources.getIdentifier(
                        "il_search_connect",
                        "drawable",
                        packageName
                    )
                    errorTextId = resources.getIdentifier(
                        "no_connetion",
                        "string",
                        packageName
                    )
                    showMessage(textId = errorTextId, imageId = errorIconId, showButton = true)
                }
            })
        }
    }

    private fun showMessage(
        textId: Int = 0,
        imageId: Int = 0,
        showButton: Boolean = false
    ) {

        placeholderMessage = findViewById(R.id.placeholderMessage)
        placeholderImage  = findViewById(R.id.errorSearchImage)
        placeholderText = findViewById(R.id.errorSearchText)
        placeholderButton = findViewById(R.id.refreshSearch)

        if (textId > 0) {
            placeholderMessage.visibility = View.VISIBLE
            placeholderImage.setImageResource(imageId)
            placeholderText.setText(textId)
            if (showButton) {
                placeholderButton.visibility = View.VISIBLE
            }
            else {
                placeholderButton.visibility = View.GONE
            }

            tracks.clear()
            trackAdapter.notifyDataSetChanged()
            rvTracks.visibility = View.GONE
        } else {
            placeholderMessage.visibility = View.GONE
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_EDITTEXT, inputEditText.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputEditText.setText(savedInstanceState.getString(SEARCH_EDITTEXT))
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}