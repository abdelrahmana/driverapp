package com.tt.driver.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity


object IntentUtils {

    fun launchGoogleMapAndMarkLocation(context: Context, lat: Double, lng: Double) {
        val intent =
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:<$lat>,<$lng>?q=<$lat>,<$lng>(${"destination"})")
            )
        context.startActivity(intent)
    }

    fun dialPhone(context: Context, phoneNumber: String) {
        try {
            context.startActivity(
                Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
            )
        } catch (exception: Exception) {
        }
    }

    fun shareUrl(context: Context, url: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
        }

        val shareTitle = "Taht-Talabak"
        context.startActivity(Intent.createChooser(sendIntent, shareTitle))
    }

}