package com.timchenko.playlistmaker

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // реализация клика на кнопку Назад
        val buttonBack = findViewById<ImageView>(R.id.back)
        buttonBack.setOnClickListener {
            this.finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            themeSwitcher.isChecked = true
        }

        themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
        }

        // кнопка "Поделиться приложением"
        val buttonShare = findViewById<TextView>(R.id.buttonShare)
        buttonShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_uri))
            intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.share_title))
            startActivity(intent)
        }

        // кнопка "Написать в поддержку"
        val buttonEmail = findViewById<TextView>(R.id.buttonEmail)
        buttonEmail.setOnClickListener {
            Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.contact_from)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_subj))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.contact_message))
                startActivity(this)
            }
        }


        // кнопка "Пользовательское соглашение"
        val buttonEula = findViewById<TextView>(R.id.buttonEula)
        buttonEula.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.eula_uri)))
            startActivity(intent)
        }
    }
}