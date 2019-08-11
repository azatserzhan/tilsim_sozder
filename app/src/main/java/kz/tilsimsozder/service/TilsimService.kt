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
import android.util.Log
import android.widget.RemoteViews
import kz.tilsimsozder.R
import kz.tilsimsozder.tilsimsozder.SharedPreference
import kz.tilsimsozder.TilsimSozderActivity
import kz.tilsimsozder.tilsimsozder.ui.TilsimsozderFragment

@Suppress("DEPRECATION")
class TilsimService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.d("service", "destroy")
        super.onDestroy()
    }

    override fun onCreate() {
        // TilsimsozderFragment.START_NOTIFICATION = false
        // TODO: раскрой коммент выше
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setupService()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun setupService() {
        Handler().postDelayed({
            val isTilsimPageActive = SharedPreference(this@TilsimService).getIsTilsimPage()
            if (!isTilsimPageActive) {
                showNotification(this@TilsimService)
            }
            setupService()

        }, 1000 * 3 * 60)
    }

    private fun getData() {
        LIST_TITLE_DATA_TILSIM = this.resources.getStringArray(R.array.tilsim_sozder_title).toMutableList()
        LIST_CONTENT_DATA_TILSIM = this.resources.getStringArray(R.array.tilsim_sozder_content).toMutableList()
    }

    @SuppressLint("NewApi")
    fun showNotification(context: Context) {
        getData()

        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: Notification.Builder
        val channelId = "kz.tilsimsozder"
        val description = "Уведомления"

        val intent = Intent(context, TilsimSozderActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val contentView = RemoteViews(packageName, R.layout.notification_layout)
        val listLength = LIST_CONTENT_DATA_TILSIM.size - 1
        RANDOM_TILSIM = (0..listLength).shuffled().last()

        contentView.setTextViewText(R.id.tv_title, LIST_TITLE_DATA_TILSIM[RANDOM_TILSIM])
        contentView.setTextViewText(R.id.tv_content, LIST_CONTENT_DATA_TILSIM[RANDOM_TILSIM])

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(context, channelId)
                .setContent(contentView)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.icon))
                .setContentIntent(pendingIntent)
        } else {
            builder = Notification.Builder(context)
                .setContent(contentView)
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.icon))
                .setContentIntent(pendingIntent)
        }

        notificationManager.notify(1234, builder.build())
    }

    companion object {
        var LIST_TITLE_DATA_TILSIM: MutableList<String> = mutableListOf()
        var LIST_CONTENT_DATA_TILSIM: MutableList<String> = mutableListOf()
        var RANDOM_TILSIM: Int = 0
    }
}
