package com.timchenko.playlistmaker.domain.impl

import com.timchenko.playlistmaker.data.ExternalNavigator
import com.timchenko.playlistmaker.domain.SharingInteractor
import com.timchenko.playlistmaker.domain.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
): SharingInteractor {
    override fun shareApp(link: String, title: String) {
        externalNavigator.shareLink(url = link, title = title)
    }

    override fun openTerms(link: String) {
        externalNavigator.openLink(url = link)
    }

    override fun openSupport(email: String, subject: String, text: String) {
        externalNavigator.openEmail(data = getSupportEmailData(
            email = email,
            subject = subject,
            text = text
        ))
    }

    private fun getSupportEmailData(email: String, subject: String, text: String): EmailData {
        return EmailData(
            email = email,
            subject = subject,
            text = text
        )
    }

}