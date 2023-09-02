package com.tt.driver.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import com.andrognito.flashbar.Flashbar
import com.andrognito.flashbar.anim.FlashAnim
import com.tt.driver.utils.Constant.CAMERA
import com.tt.driver.utils.Constant.GALLERY
import com.waysgroup.t7t_talbk_driver.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Util {
    fun checkPermssionGrantedForImageAndFile(
        context: Activity,
        requestPermssions: ActivityResultLauncher<Array<String>>
    ) : Boolean {
        var allow = false
        // imageView.setOnClickListener {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermssions.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                )
            }else{
                allow = true

            }
        } else {
            // startCameraNow()
            allow =  true

        }
        return allow
    }
    fun picPhoto(
        activity: Activity,
        onActivityResult: ActivityResultLauncher<Intent>,
        registerGallery: ActivityResultLauncher<Intent>
    ) {
        val str = arrayOf(activity.getString(R.string.camera_option), activity.getString(R.string.gallery)) // which if 1
        AlertDialog.Builder(activity).setItems(
            str) { dialog, which -> performImgPicAction(which,onActivityResult,registerGallery)}.show()

    }
    fun performImgPicAction(
        which: Int,
        onActivityResult: ActivityResultLauncher<Intent>,
        registerGallery: ActivityResultLauncher<Intent>,
        registerVideo: ActivityResultLauncher<Intent>? =null
    ) {
        val intent: Intent?
        if (which == GALLERY) {  // in case we need to get image from gallery
            intent = Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.putExtra(Constant.WHICHSELECTION, GALLERY)
            registerGallery.launch(intent)

        }
        else {  // in case we need camera
            intent = Intent()
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE)

            intent.putExtra(Constant.WHICHSELECTION, CAMERA)
            onActivityResult.launch(intent)


        }
    }
    fun getCreatedFileFromBitmap(fileName: String, bitmapUpdatedImage: Bitmap, typeOfFile : String?, context: Context) : File {
        val bytes =  ByteArrayOutputStream()
        bitmapUpdatedImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        /*   val f = new File(Environment.getExternalStorageDirectory()
                   + File.separator + "testimage.jpg");*/
        val f =  initFile(fileName,typeOfFile?:"jpg",context)
        f?.createNewFile()
        val fo =  FileOutputStream(f)
        fo.write(bytes.toByteArray())
        fo.close()
        return f!!
    }
    fun initFile(name :String,type : String,context: Context): File? {  // to delete file you need to get the absoloute paths for it and it's directory
        var file : File? = null // creating file for video
        file = File( context.cacheDir.absolutePath, SimpleDateFormat(
            "'$name'yyyyMMddHHmmss'.$type'", Locale.ENGLISH).format(Date()))
        //}
        return file
    }
    fun showSnackMessages(
        activity: Activity?,
        error: String?,color : Int= android.R.color.holo_red_dark
    ) {
        if (activity != null) {
            Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.TOP)
                //.title(activity.getString(R.string.errors))
                .message(error!!)
                .backgroundColorRes(color)
                .dismissOnTapOutside()
                .duration(2500)
                .enableSwipeToDismiss()
                .enterAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(550)
                        .alpha()
                        .overshoot()
                )
                .exitAnimation(
                    FlashAnim.with(activity)
                        .animateBar()
                        .duration(200)
                        .anticipateOvershoot()
                )
                .build().show()
        }
    }
}