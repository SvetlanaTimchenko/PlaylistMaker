package com.timchenko.playlistmaker.ui.settings

import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.databinding.FragmentSettingsBinding
import com.timchenko.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModel()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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