package kz.tilsimsozder.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.RemoteViews
import kz.tilsimsozder.MainActivity
import kz.tilsimsozder.R

class MyService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        MainActivity.START_NOTIFICATION = false
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setupService()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun setupService(){
        Handler().postDelayed({
            showNotification(this@MyService)
            setupService()
        }, 1000*60*3)
    }

    @SuppressLint("NewApi")
    fun showNotification(context: Context){
        var notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        lateinit var notificationChannel : NotificationChannel
        lateinit var builder : Notification.Builder
        val channelId = "kz.tilsimsozder"
        val description = "Уведомления"

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val contentView = RemoteViews(packageName, R.layout.notification_layout)
        val listLength = MainActivity.LIST_CONTENT_DATA.size - 1

        contentView.setTextViewText(R.id.tv_title, MainActivity.LIST_TITLE_DATA[(0..listLength).shuffled().last()])
        contentView.setTextViewText(R.id.tv_content,MainActivity.LIST_CONTENT_DATA[(0..listLength).shuffled().last()])

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId,description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(context,channelId)
                .setContent(contentView)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.icon))
                //.setContentIntent(pendingIntent)
        }else{
            builder = Notification.Builder(context)
                .setContent(contentView)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.icon))
                //.setContentIntent(pendingIntent)
        }

        notificationManager.notify(1234,builder.build())
    }
}
