package kz.tilsimsozder.service

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
import kz.tilsimsozder.TilsimSozderActivity
import kz.tilsimsozder.preference.SharedPreference
import kz.tilsimsozder.preference.SupportLanguage

class TilsimService : Service() {

    private var tilsimsTitle: MutableList<String> = mutableListOf()
    private var tilsimsBody: MutableList<String> = mutableListOf()
    private var randomTilsim: Int = 0

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        Log.d("service", "destroy")
        super.onDestroy()
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
        tilsimsTitle = when (SharedPreference(baseContext).getLanguageCode()) {
            SupportLanguage.KZ.code -> this.resources.getStringArray(R.array.tilsim_sozder_title).toMutableList()
            SupportLanguage.UZ.code -> this.resources.getStringArray(R.array.tilsim_sozder_title_uz).toMutableList()
            SupportLanguage.RU.code -> this.resources.getStringArray(R.array.tilsim_sozder_title_ru).toMutableList()
            else -> this.resources.getStringArray(R.array.tilsim_sozder_title).toMutableList()
        }

        tilsimsBody = when (SharedPreference(baseContext).getLanguageCode()) {
            SupportLanguage.KZ.code -> this.resources.getStringArray(R.array.tilsim_sozder_content).toMutableList()
            SupportLanguage.UZ.code -> this.resources.getStringArray(R.array.tilsim_sozder_content_uz).toMutableList()
            SupportLanguage.RU.code -> this.resources.getStringArray(R.array.tilsim_sozder_content_ru).toMutableList()
            else -> this.resources.getStringArray(R.array.tilsim_sozder_content).toMutableList()
        }
    }

    private fun showNotification(context: Context) {
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
        val listLength = tilsimsBody.size - 1
        randomTilsim = (0..listLength).shuffled().last()

        contentView.setTextViewText(R.id.tv_title, tilsimsTitle[randomTilsim])
        contentView.setTextViewText(R.id.tv_content, tilsimsBody[randomTilsim])

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
}
