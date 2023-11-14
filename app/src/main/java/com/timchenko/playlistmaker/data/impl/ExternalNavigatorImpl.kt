package com.timchenko.playlistmaker.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.timchenko.playlistmaker.data.ExternalNavigator
import com.timchenko.playlistmaker.domain.models.EmailData

class ExternalNavigatorImpl(
    private val context: Context
) : ExternalNavigator {

    override fun shareLink(url: String, title: String) {
        val intent = Intent(Intent.ACTION_SEND).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, url)
        intent.putExtra(Intent.EXTRA_TITLE, title)
        startAction(intent)

    }

    override fun sharePlaylist(message: String) {
        val intent = Intent(Intent.ACTION_SEND).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, message)
        startAction(intent)
    }

    override fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startAction(intent)
    }

    override fun openEmail(data: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:")).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(data.email))

        intent.putExtra(Intent.EXTRA_SUBJECT, data.subject)
        intent.putExtra(Intent.EXTRA_TEXT, data.text)
        startAction(intent)
    }

    private fun startAction(intent: Intent) {
        try {
            context.startActivity(intent)
        } catch (error: java.lang.Exception) {
            Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
        }
    }
}
