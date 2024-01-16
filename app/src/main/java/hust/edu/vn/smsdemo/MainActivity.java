package hust.edu.vn.smsdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.RemoteInput;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaTimestamp;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtMobile;
    private EditText txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMobile = (EditText) findViewById(R.id.mblTxt);
        txtMessage = (EditText) findViewById(R.id.msgTxt);
        Button btnSms = findViewById(R.id.btnSend);
        Button button = (Button) findViewById(R.id.button2);
        // check quyền để gưi
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.POST_NOTIFICATIONS)!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                        Manifest.permission.POST_NOTIFICATIONS},101
                );
            }
        }


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    makeNotify();
                }
            });
        btnSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SmsManager smgr = SmsManager.getDefault();
                    smgr.sendTextMessage(txtMobile.getText().toString(), null, txtMessage.getText().toString(), null, null);
                    Toast.makeText(MainActivity.this, "SMS Sent Successfully", Toast.LENGTH_SHORT).show();
                    Toast.makeText(MainActivity.this, txtMobile.getText().toString() + " : " + txtMessage.getText().toString(), Toast.LENGTH_LONG).show();
                    sendSMS(txtMobile.getText().toString(), txtMessage.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnSms.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("smsto:"));
                    i.setType("vnd.android-dir/mms-sms");
                    i.putExtra("address", new String(txtMobile.getText().toString()));
                    i.putExtra("sms_body", txtMessage.getText().toString());
                    startActivity(Intent.createChooser(i, "Send sms via:"));
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "SMS Failed to Send, Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendSMS(String phoneNumber, String msg) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, msg, null, null);
    }

    public void makeNotify() {
        final String channel_id = "CHANNEL_ID";
        RemoteInput remoteInput = new RemoteInput.Builder("key_text_reply")
                .setLabel("Your answer....")
                .build();
        Intent replyIntent = new Intent(this , MyReceiver.class);
        PendingIntent pendingReplyIntent  = PendingIntent.getBroadcast(this,0,replyIntent,
                    PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_reply,"Reply",pendingReplyIntent).addRemoteInput(remoteInput).build();
        NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("me");
        messagingStyle.setConversationTitle("Group chat");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Message")
                .setContentText("Hello How are you ?")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .addAction(replyAction)
                .setColor(Color.BLUE);


        // Xử lý khi bấm vào notification
        // chuyển sang activity mới
        Intent intent = new Intent(this, MakeNotification.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channel_id, "Message", importance);
            channel.setDescription("This is message");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
            managerCompat.notify(0, builder.build());


        }
    }
}
