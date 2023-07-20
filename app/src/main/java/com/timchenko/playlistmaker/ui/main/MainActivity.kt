package com.timchenko.playlistmaker.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.timchenko.playlistmaker.ui.media.MediaActivity
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.presentation.main.MainViewModel
import com.timchenko.playlistmaker.ui.search.SearchActivity
import com.timchenko.playlistmaker.ui.settings.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.setAppTheme()

        // реализация клика на кнопку Поиск
        val buttonSearch = findViewById<Button>(R.id.search_btn)
        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }

        // реализация клика на кнопку Медиа
        val buttonMedia = findViewById<Button>(R.id.media_btn)
        buttonMedia.setOnClickListener {
            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }

        // реализация клика на кнопку Настройки
        val buttonSettings = findViewById<Button>(R.id.settings_btn)
        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }
}