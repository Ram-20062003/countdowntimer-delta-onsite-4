package com.example.countdowntimer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Time_Dialog.Time_Dialog_DataExchange {
    Fragment fragment;
   public static Button b_set,b_stop,b_cancel,b_start;
  public static ImageButton b_0,b_20,b_pause,b_restart,b_resume;
    TextView textView;
    int h,m,s;
    public static long pause=0;
    public static long resume=0;
    public static int click=0;
    private static final String TAG = "MainActivity";
    ProgressBar progressBar;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b_set=findViewById(R.id.set);
        b_20=findViewById(R.id.plus10);
        b_0=findViewById(R.id.minus10);
        b_stop=findViewById(R.id.stop);
        b_start=findViewById(R.id.start);
        b_pause=findViewById(R.id.pause);
        b_restart=findViewById(R.id.restart);
        b_cancel=findViewById(R.id.cancel);
        b_resume=findViewById(R.id.resume);
        b_set.setVisibility(View.VISIBLE);
        textView=findViewById(R.id.time_text);
        progressBar=findViewById(R.id.progressBar);
        fragment=new Fragment();
        b_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Time_Dialog time_dialog=new Time_Dialog();
               time_dialog.show(getSupportFragmentManager(),"time_dialog");
               b_start.setVisibility(View.VISIBLE);
               b_stop.setVisibility(View.GONE);
            }
        });
        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click=0;
                b_start.setVisibility(View.GONE);
                b_stop.setVisibility(View.VISIBLE);
                intent=new Intent(MainActivity.this,TImer_serviceclass.class);
                intent.putExtra("hour",h);
                intent.putExtra("minute",m);
                intent.putExtra("second",s);
                startService(intent);

            }
        });
        b_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TImer_serviceclass.countDownTimer.cancel();

                stopService(intent);
            }
        });
        b_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click++;
                Intent intent=new Intent(MainActivity.this,TImer_serviceclass.class);
                intent.putExtra("minus",1);
                startService(intent);
            }
        });
        b_20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click--;
                Intent intent=new Intent(MainActivity.this,TImer_serviceclass.class);
                intent.putExtra("minus",1);
                startService(intent);

            }
        });
        b_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause=System.currentTimeMillis();
                b_pause.setVisibility(View.GONE);
                b_resume.setVisibility(View.VISIBLE);
                pause=System.currentTimeMillis();
                Intent intent=new Intent(MainActivity.this,TImer_serviceclass.class);
                intent.putExtra("pause",1);
                startService(intent);

            }
        });
        b_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b_resume.setVisibility(View.GONE);
                b_pause.setVisibility(View.VISIBLE);
                intent.putExtra("pause",2);
                startService(intent);
            }
        });
        b_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TImer_serviceclass.countDownTimer.cancel();
                stopService(intent);
                intent.putExtra("pause",5);
                startService(intent);

            }
        });
        if(getIntent().getAction().equals("action_yes"))
            Log.d(TAG, "onCreate: yesyesy");
LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver,new IntentFilter("time"));
    }

    @Override
    public void apply(int hour, int minute, int second) {
        h=hour;
        m=minute;
        s=second;

        //textView.setText(hour+":"+minute+":"+second);
        Log.d(TAG, "apply: "+hour+minute+second);
    }

    BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            textView.setText(intent.getStringExtra("time"));
            int tot=(h*3600+m*60+s)*1000;
            progressBar.setMax(tot);
            progressBar.setProgress((int) intent.getLongExtra("millis",0));
            pause=progressBar.getProgress();
            String action=intent.getAction();
            if(action.equals("yes"))
                Log.d(TAG, "onReceive: yes");
        }
    };
}