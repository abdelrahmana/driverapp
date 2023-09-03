package com.tt.driver.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import java.net.URL


object MapHelper {

    fun getPinIconFromUrl(
        url: String?,
        context: Context,
        success: (BitmapDescriptor?) -> Unit
    ) {
        url ?: return success(null)
        try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        val sizedBitmap = Bitmap.createScaledBitmap(resource, 150, 150, false)
                        success(BitmapDescriptorFactory.fromBitmap(sizedBitmap))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}

                })
        } catch (e: Exception) {
            Log.e("UrlToDrawableError", e.stackTraceToString())
            success(null)
        }
    }

}