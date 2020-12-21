package com.h.itservices.notifications;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.h.itservices.R;
import com.h.itservices.activities.AddCommentActivity;
import com.h.itservices.activities.ChatActivity;


public class FirebaseMessagingServices extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        final int random =(int) SystemClock.currentThreadTimeMillis();





        Intent intent =null;


        if(remoteMessage.getData().get("type").equalsIgnoreCase("Question")) {

            intent = new Intent(FirebaseMessagingServices.this, AddCommentActivity.class);
            intent.putExtra("question_id",remoteMessage.getData().get("question_id"));
            intent.putExtra("publisher_uid",remoteMessage.getData().get("publisher_uid"));
            intent.putExtra("publisher_name",remoteMessage.getData().get("publisher_name"));
            intent.putExtra("qa_titel",remoteMessage.getData().get("qa_titel"));
            intent.putExtra("qa_text",remoteMessage.getData().get("qa_text"));
            intent.putExtra("type","Question");

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            PendingIntent pendingIntent =    PendingIntent.getActivity(this, (int) SystemClock.currentThreadTimeMillis() /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            String channelId = getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.logo)
                            .setTicker(remoteMessage.getData().get("decription"))
                            .setContentTitle(remoteMessage.getData().get("title"))
                            .setContentText(remoteMessage.getData().get("decription")  )
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify((int) SystemClock.currentThreadTimeMillis(), notificationBuilder.build());





        }else {

            intent = new Intent(FirebaseMessagingServices.this, ChatActivity.class);
            intent.putExtra("decription",remoteMessage.getData().get("decription"));
            intent.putExtra("publisher_id",remoteMessage.getData().get("publisher_id"));
            intent.putExtra("qa_id",remoteMessage.getData().get("qa_id"));
            intent.putExtra("name",remoteMessage.getData().get("name"));
            intent.putExtra("type","notification");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            PendingIntent pendingIntent =    PendingIntent.getActivity(this, (int) SystemClock.currentThreadTimeMillis() /* Request code */, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            String channelId = getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.logo)
                            .setTicker(remoteMessage.getData().get("decription"))
                            .setContentTitle(remoteMessage.getData().get("title"))
                            //.setWhen(112)
                            .setContentText(remoteMessage.getData().get("decription")  )


                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify((int) SystemClock.currentThreadTimeMillis(), notificationBuilder.build());







        }









    }




    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();

    }


    @Override
    public void onNewToken(String token) {


    }





}
