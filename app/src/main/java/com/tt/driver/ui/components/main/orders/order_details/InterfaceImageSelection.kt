package com.tt.driver.ui.components.main.orders.order_details

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.tt.driver.utils.Util

interface InterfaceImageSelection {
 fun setImagePath(bitmap: Bitmap?)
 fun getImagePath () : String?
 }
class  ImplementerSelectedImage(val imageView: ImageView,val context : Context) :InterfaceImageSelection{
   var path : String? =null
    override fun setImagePath(bitmap: Bitmap?) {
        bitmap?.let {
        imageView.setImageBitmap(bitmap)
            path =   Util.getCreatedFileFromBitmap("image",bitmap,"jpg",context).absolutePath
        }
    }


    override fun getImagePath(): String? {
       return path
    }

}