package com.example.yourmessage.messaging

import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.yourmessage.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // save token for user if needed
        Log.e("on new token",token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("on message recieved", remoteMessage.notification?.title.toString())
        showNotification(remoteMessage)
    }

    fun showNotification(remoteMessage: RemoteMessage){
        Log.e("show not", remoteMessage.notification?.body.toString())

        var builder = NotificationCompat.Builder(this, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_action_name)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
         if(remoteMessage.notification?.imageUrl==null) {

             builder.setStyle(
                 NotificationCompat.BigTextStyle()
                     .bigText("Much longer text that cannot fit one line...")
             )
                 .setPriority(NotificationCompat.PRIORITY_DEFAULT)
         }else{
             val bitmap : Bitmap = loadImageToBitMap(remoteMessage)
             builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                 .setPriority(NotificationCompat.PRIORITY_DEFAULT)
         }
        val notManegers =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notManegers.notify(10,builder.build())

    }

    private fun loadImageToBitMap(remoteMessage: RemoteMessage): Bitmap {
       var bitmap:Bitmap? = null
     Glide.with(this)
         .asBitmap().load(remoteMessage.notification?.imageUrl)
         .addListener(object : RequestListener<Bitmap> {
             override fun onLoadFailed(
                 e: GlideException?,
                 model: Any?,
                 target: Target<Bitmap>?,
                 isFirstResource: Boolean
             ): Boolean {
                 TODO("Not yet implemented")
                 return false
             }

             override fun onResourceReady(
                 resource: Bitmap?,
                 model: Any?,
                 target: Target<Bitmap>?,
                 dataSource: DataSource?,
                 isFirstResource: Boolean
             ): Boolean {
                bitmap=resource!!
                return  true
             }
         })

 return  bitmap!!
    }


}