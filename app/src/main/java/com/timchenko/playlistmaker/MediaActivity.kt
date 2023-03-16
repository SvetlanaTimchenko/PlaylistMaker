package com.timchenko.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)

        // реализация клика на кнопку Назад
        val buttonBack = findViewById<ImageView>(R.id.back)
        buttonBack.setOnClickListener {
            this.finish()
        }
    }
}