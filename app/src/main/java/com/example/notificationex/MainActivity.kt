package com.example.notificationex

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val PRIMARY_CHANNNEL_ID:String="Primary notification channel"
    lateinit var mNotifyManager:NotificationManager
    val NOTIFICATION_ID=0
    lateinit var innerVar:NotificationReceiver
    var mReceiver:NotificationReceiver=NotificationReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setNotificationButtonState(true, false, false)

        var button_notify :Button
        button_notify=btn_notifyMe

        innerVar =NotificationReceiver()

        mNotifyManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        button_notify.setOnClickListener(View.OnClickListener() {
            sendNotification()
        })

        createNotificationChannel()

        btn_update.setOnClickListener(View.OnClickListener() {
            updateNotification()
        })

        btn_cancel.setOnClickListener(View.OnClickListener() {
            cancelNotification()
        })

        registerReceiver(mReceiver,IntentFilter(innerVar.ACTION_UPDATE_NOTIFICATION))

    }

    fun sendNotification(){
        val updateIntent = Intent(innerVar.ACTION_UPDATE_NOTIFICATION)
        val updatePendingIntent = PendingIntent.getBroadcast(
            this,
            NOTIFICATION_ID,
            updateIntent,
            PendingIntent.FLAG_ONE_SHOT
        )

        setNotificationButtonState(false, true, true)
        var notifyBuilder:NotificationCompat.Builder=getNotificationBuilder()
        mNotifyManager.notify(NOTIFICATION_ID,notifyBuilder.build())

        notifyBuilder.addAction(R.drawable.ic_update, "Update Notification", updatePendingIntent)

    }

    fun createNotificationChannel(){

        if(android.os.Build.VERSION.SDK_INT>=android.os.Build.VERSION_CODES.O){
            //Create notification channel
            var notificationChannel=NotificationChannel(PRIMARY_CHANNNEL_ID,"Mascot Notification",NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);

        }

    }

    fun getNotificationBuilder():NotificationCompat.Builder{

        var notificationIntent:Intent=Intent(this,MainActivity::class.java)
        var notificationPendingIntent:PendingIntent= PendingIntent.getActivity(this,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        var notifyBuilder=NotificationCompat.Builder(this,PRIMARY_CHANNNEL_ID)
            .setContentTitle("You've been notified!")
            .setContentText("This is your notification text.")
            .setSmallIcon(R.drawable.ic_android)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL);

        return notifyBuilder
    }

    fun updateNotification():Unit{
        setNotificationButtonState(false, false, true)
        var notifyBuilder=getNotificationBuilder();

        notifyBuilder.setContentTitle("Update Available")
            .setContentText("Version 3.2.11")

        mNotifyManager.notify(NOTIFICATION_ID,notifyBuilder.build())

    }

    fun cancelNotification():Unit{
        setNotificationButtonState(true, false, false)
        mNotifyManager.cancel(NOTIFICATION_ID)

    }

    fun setNotificationButtonState(
        isNotifyEnabled: Boolean,
        isUpdateEnabled: Boolean,
        isCancelEnabled: Boolean
    ) {
        btn_notifyMe.setEnabled(isNotifyEnabled)
        btn_update.setEnabled(isUpdateEnabled)
        btn_cancel.setEnabled(isCancelEnabled)
    }


    inner class NotificationReceiver:BroadcastReceiver(){
        public val ACTION_UPDATE_NOTIFICATION:String="com.example.android.notifyme.ACTION_UPDATE_NOTIFICATION"

        override fun onReceive(context: Context?, intent: Intent?) {
            //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }



    }

}
