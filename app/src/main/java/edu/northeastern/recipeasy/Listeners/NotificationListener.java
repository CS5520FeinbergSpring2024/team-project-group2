package edu.northeastern.recipeasy.Listeners;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.northeastern.recipeasy.R;
import edu.northeastern.recipeasy.activities.HomePage;
import edu.northeastern.recipeasy.activities.InboxActivity;
import edu.northeastern.recipeasy.activities.MessageActivity;
import edu.northeastern.recipeasy.domain.Message;
import edu.northeastern.recipeasy.utils.DataUtil;

public class NotificationListener {

    private DatabaseReference newMessageRef;
    private ValueEventListener listener;
    private Context context;
    private NotificationManager notificationManager;
    private String currentUser;
    private static final int NOTIFICATION_REQUEST_CODE = 101;

    public NotificationListener(String currentUsername, Context context) {
        this.context = context;
        this.currentUser = currentUsername;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        createNotificationChannel(
                "edu.northeastern.recipeasy",
                "MessageNotification",
                "New Message"
        );

        requestPermission(android.Manifest.permission.POST_NOTIFICATIONS, NOTIFICATION_REQUEST_CODE);
        newMessageRef = FirebaseDatabase.getInstance().getReference().child("messages").child(currentUsername);
        startListener();
    }

    private void startListener() {
        new Thread(() -> {
            try{
                listener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // Should send a notification here
                        ArrayList<Message> receivedMessages = DataUtil.parseMessages(dataSnapshot);
                        for (int i = 0; i< receivedMessages.size(); i++){
                            if (!receivedMessages.get(i).isSentNotification()) {
                                sendNotification(receivedMessages.get(i).getSenderUsername(), receivedMessages.get(i).getMessage(), i);
                                receivedMessages.get(i).setSentNotification(true);
                            }
                        }
                        newMessageRef.setValue(receivedMessages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                };

                newMessageRef.addValueEventListener(listener);
            } catch (Exception e) {

            }
        }).start();
    }

    public void unregisterListener() {
        if (listener != null) {
            newMessageRef.removeEventListener(listener);
        }
    }


    private void sendNotification(String sender, String content, int notificationId){
        String channelId = "edu.northeastern.recipeasy";

        Intent resultIntent = new Intent(context, MessageActivity.class);
        resultIntent.putExtra("currentUsername", currentUser);
        resultIntent.putExtra("otherUsername", sender);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,
                resultIntent, PendingIntent.FLAG_IMMUTABLE);


        Notification notification = new Notification.Builder(this.context, channelId)
                .setContentTitle(sender.toUpperCase() + " sent you a message:")
                .setContentText(content)
                .setSmallIcon(R.drawable.recipeasylogo)
                .setChannelId(channelId)
                .setContentIntent(pendingIntent)
                .build();

        notificationManager.notify(notificationId, notification);

    }


    protected void createNotificationChannel(String id, String name, String description){
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(id, name, importance);

        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern( new long[] {100,200,300,400, 500, 400, 300, 200, 400});
        notificationManager.createNotificationChannel(channel);
    }

    protected void requestPermission(String permissionType, int requestCode){
        int permission = ContextCompat.checkSelfPermission(this.context, permissionType);
        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions((Activity) this.context, new String[] {permissionType}, requestCode);
        }
    }

}
