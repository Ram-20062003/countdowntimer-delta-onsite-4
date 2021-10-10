package com.example.countdowntimer;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TImer_serviceclass extends Service {
    private static final String TAG = "TImer_serviceclass";
    public static CountDownTimer countDownTimer;
    int sub,time_left;
    String time;
    public static long time_resume;
    int b=1;
    int i;
    long hour_left,minute_left,seconds_left;
    public TImer_serviceclass() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: Service going");
        int h=intent.getIntExtra("hour",0);
        int m=intent.getIntExtra("minute",0);
        int s=intent.getIntExtra("second",0);
        i=intent.getIntExtra("pause",0);
        Log.d(TAG, "i: "+i);
        //time=String.format(Locale.getDefault(),"%02d:%02d:%02d",h,m,s);
         sub=intent.getIntExtra("minus",0);
        Log.d(TAG, "onStartCommand: "+h+m+s);
         time_left=(h*3600+m*60+s)*1000;
         if(i==2)
             time_left= (int) time_resume;
        countDownTimer=new CountDownTimer(time_left,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Intent intent_save=new Intent("time");
                if(sub==1)
                {
                    millisUntilFinished=millisUntilFinished-(MainActivity.click*10000);
                }
                if(i==1)
                { intent_save.putExtra("time",time);
                    intent_save.putExtra("millis",millisUntilFinished);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent_save);
                    cancel();
                }
                else {

                    long lapse=MainActivity.resume-MainActivity.pause;
                    time_resume=millisUntilFinished;
                    hour_left = ((millisUntilFinished) / 1000) / 3600;
                    minute_left = ((millisUntilFinished) / 1000) / 60;
                    seconds_left = ((millisUntilFinished) / 1000) % 60;
                     time=String.format(Locale.getDefault(),"%02d:%02d:%02d",hour_left,minute_left,seconds_left);
                    intent_save.putExtra("time",time);
                    intent_save.putExtra("millis",millisUntilFinished);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent_save);
                }
                if((hour_left==0&&minute_left==0&&seconds_left==0)||(hour_left==0&&minute_left==0&&seconds_left<0)) {
                    time="00:00:00";
                    intent_save.putExtra("millis",0);
                    intent_save.putExtra("time",0);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent_save);
                    cancel();
                    stopSelf();
                }


                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent_save);
                Log.d(TAG, "onTick: "+seconds_left);
                Intent intent1=new Intent(TImer_serviceclass.this, Broadcastreceiver.class);
                intent1.setAction("action_yes");
                intent1.putExtra("yes",1);
                PendingIntent pendingIntent=PendingIntent.getBroadcast(TImer_serviceclass.this,0,intent1,0);
                Intent intent2=new Intent(TImer_serviceclass.this, Broadcastreceiver.class);
                intent2.setAction("action_stop");
                PendingIntent pendingIntent2=PendingIntent.getBroadcast(TImer_serviceclass.this,0,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
               NotificationChannel channel = new NotificationChannel("Ram", "name", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("description");
                @SuppressLint("RemoteViewLayout") RemoteViews collapsedView=new RemoteViews(getPackageName(),R.layout.customnotification);
                collapsedView.setTextViewText(R.id.time,time);
                collapsedView.setOnClickPendingIntent(R.id.pause_button,pendingIntent);
               collapsedView.setOnClickPendingIntent(R.id.stop_button,pendingIntent2);
               collapsedView.setProgressBar(R.id.progressBar2,time_left, (h*3600+m*60+s)*1000-(int) millisUntilFinished,false);
                NotificationManager notificationManager1 = getSystemService(NotificationManager.class);
                notificationManager1.createNotificationChannel(channel);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Ram")
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Time-left")
                        .setContentText(time).setStyle(new NotificationCompat.DecoratedCustomViewStyle()).setCustomBigContentView(collapsedView)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                notificationManager.notify(1, builder.build());
                startForeground(1, builder.build());

            }

            @Override
            public void onFinish() {

            }
        }.start();
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onStartCommand: Service going IN ONCREATE");

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onStartCommand: Service ended");

    }


}