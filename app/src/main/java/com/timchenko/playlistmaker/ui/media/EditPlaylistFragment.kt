package com.timchenko.playlistmaker.ui.media

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.timchenko.playlistmaker.R
import com.timchenko.playlistmaker.presentation.media.EditPlaylistFragmentViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment: AddPlaylistFragment() {
    override val viewModel: EditPlaylistFragmentViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistID = requireArguments().getInt(PLAYLIST_ID, 0)
        if (playlistID > 0) {
            viewModel.getPlaylist(playlistID)
        }
        else {
            findNavController().popBackStack()
        }
        viewModel.observePlaylist().observe(viewLifecycleOwner) {
            binding.inputEdittextName.setText(it.name)
            binding.inputEdittextDescription.setText(it.description)
            if (it.uri != null) {
                Glide.with(this)
                    .load(it.uri)
                    .placeholder(R.drawable.placeholderph)
                    .transform(CenterCrop())
                    .into(binding.playlistImage)
            }
            tempPlaylist = it
        }
        binding.newPlaylistHeader.text = getText(R.string.edit)
        binding.createButton.text = getText(R.string.save)

        binding.createButton.setOnClickListener {
            lifecycleScope.launch { viewModel.savePlaylist(tempPlaylist) }
            findNavController().popBackStack()
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )
    }

    companion object {
        private const val PLAYLIST_ID = "playlistID"
        fun createArgs(playlistId: Int?): Bundle =
            bundleOf(PLAYLIST_ID to playlistId)
    }
}