package com.timchenko.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // реализация клика на кнопку Поиск через анонимный класс
        val buttonSearch = findViewById<Button>(R.id.search_btn)
        val buttonSearchClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Нажали на кнопку Поиск", Toast.LENGTH_LONG).show()
            }
        }
        buttonSearch.setOnClickListener(buttonSearchClickListener)

        // реализация клика на кнопку Медиа через лябда-выражение
        val buttonMedia = findViewById<Button>(R.id.media_btn)
        buttonMedia.setOnClickListener {
            Toast.makeText(this@MainActivity, "Нажатие на кнопку Медиа!", Toast.LENGTH_SHORT).show()
        }

        // реализация клика на кнопку Настройки через анонимный класс
        val buttonSettings = findViewById<Button>(R.id.settings_btn)
        val buttonSettingsClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Нажание на кнопку Настройки", Toast.LENGTH_LONG).show()
            }
        }
        buttonSettings.setOnClickListener(buttonSettingsClickListener)
    }
}