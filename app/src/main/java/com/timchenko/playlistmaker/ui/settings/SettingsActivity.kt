package com.timchenko.playlistmaker.ui.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.res.Configuration
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.databinding.ActivitySettingsBinding
import com.timchenko.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // реализация клика на кнопку Назад
        binding.back.setOnClickListener {
            this.finish()
        }

        // переключение темы
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            binding.themeSwitcher.isChecked = true
        }
        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.updateThemeSettings(checked)
        }

        // кнопка "Поделиться приложением"
        binding.buttonShare.setOnClickListener {
            viewModel.shareApp(
                url = getString(R.string.share_uri),
                title = getString(R.string.share_title))
        }

        // кнопка "Написать в поддержку"
        binding.buttonEmail.setOnClickListener {
            viewModel.openSupport(
                email = getString(R.string.contact_from),
                subject = getString(R.string.contact_subj),
                text = getString(R.string.contact_message)
            )
        }


        // кнопка "Пользовательское соглашение"
        binding.buttonEula.setOnClickListener {
            viewModel.openTerms(url = getString(R.string.eula_uri))
        }
    }
}