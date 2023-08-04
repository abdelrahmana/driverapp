package com.tt.driver.data.services

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tt.driver.data.datastore.AuthDataStore
import com.tt.driver.data.managers.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import javax.inject.Inject

@AndroidEntryPoint
class NotificationsService : FirebaseMessagingService() {

    @Inject
    lateinit var authDataStore: AuthDataStore

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        NotificationHelper.pushNotification(
            this,
            message.notification?.title ?: "",
            message.notification?.body ?: ""
        )
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        authDataStore.setFCMToken(GlobalScope, token)
    }

}