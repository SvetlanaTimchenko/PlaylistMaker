package com.timchenko.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // реализация клика на кнопку Назад
        val buttonBack = findViewById<ImageView>(R.id.back)
        buttonBack.setOnClickListener {
            this.finish()
        }

        // кнопка "Поделиться приложением"
        val buttonShare = findViewById<TextView>(R.id.buttonShare)
        buttonShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/android-developer/")
            intent.putExtra(Intent.EXTRA_TITLE, "Android-разработка от Яндекс Практикума")
            startActivity(intent)
        }

        // кнопка "Написать в поддержку"
        val buttonEmail = findViewById<TextView>(R.id.buttonEmail)
        buttonEmail.setOnClickListener {
            val message = "Спасибо разработчикам и разработчицам за крутое приложение!"
            val subj = "Сообщение разработчикам и разработчицам приложения Playlist Maker"
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("svetlana010785@yandex.ru"))
            intent.putExtra(Intent.EXTRA_SUBJECT, subj)
            intent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(intent)
        }


        // кнопка "Пользовательское соглашение"
        val buttonEula = findViewById<TextView>(R.id.buttonEula)
        buttonEula.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/"))
            startActivity(intent)
        }
    }
}