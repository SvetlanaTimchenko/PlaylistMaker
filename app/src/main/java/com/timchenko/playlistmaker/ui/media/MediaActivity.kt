package com.timchenko.playlistmaker.ui.media

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.databinding.ActivityMediaBinding

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMediator.attach()

        // реализация клика на кнопку Назад
        binding.back.setOnClickListener {
            this.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}