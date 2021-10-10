package com.example.countdowntimer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

public class Broadcastreceiver extends BroadcastReceiver {
    private static final String TAG = "Broadcastreceiver";
    Intent intent_pause;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: hello");
        intent_pause = new Intent(context, TImer_serviceclass.class);
        if(intent.getAction().equals("action_yes")) {
            MainActivity.b_resume.setVisibility(View.VISIBLE);
            MainActivity.b_pause.setVisibility(View.GONE);
            intent_pause.putExtra("pause", 1);
            context.startService(intent_pause);
        }
            Log.d(TAG, "onReceive: yes");
        if(intent.getAction().equals("action_stop")) {
            Log.d(TAG, "onReceive: known");
            TImer_serviceclass.countDownTimer.cancel();
            context.stopService(intent_pause);
        }
    }
}
