package br.com.myself.presentation.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import br.com.myself.R
import br.com.myself.presentation.ui.financas.FinancasActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*

class Notification: FirebaseMessagingService() {
    companion object {

        private val CHANNEL_ID: String = "channel_id"
        private val CHANNEL_NAME = "channel_name"
        private val CHANNEL_DESCRIPTION = "channel_description"
        private val notificationId: Int = 0

        private fun createNotificationChannel(context: Context) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.description =
                    CHANNEL_DESCRIPTION

                // Register the channel with the system
                with(NotificationManagerCompat.from(context)) {
                    createNotificationChannel(channel)
                }
            }
        }

        fun notificar(context: Context) {
            val intent = Intent(context, FinancasActivity::class.java).apply {
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                action = "abrir_adicionar_gasto"
            }
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("Gastou alguma coisa?")
                .setContentText("Coloque aqui no seu registro, não esqueça!")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setWhen(Date().time)
    
            createNotificationChannel(context)
            
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, notificationBuilder.build())
            }

        }
        
    }
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val intent = Intent(applicationContext, FinancasActivity::class.java).apply {
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            action = "abrir_adicionar_gasto"
        }
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, 0)
    
        val notificationBuilder = NotificationCompat.Builder(applicationContext,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(remoteMessage.notification!!.title)
            .setContentText(remoteMessage.notification!!.body)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setWhen(Date().time)
    
        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, notificationBuilder.build())
        }
    }
    
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        
    }
}