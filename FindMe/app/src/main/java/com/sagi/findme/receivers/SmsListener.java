package com.sagi.findme.receivers;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.sagi.findme.R;
import com.sagi.findme.entities.Location;
import com.sagi.findme.utilities.SharedPreferencesHelper;


public class SmsListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        checkIfThisMessageIsCode(context, msgBody, msg_from);
                        Log.d("SmsListener", "msg_from: " + msg_from + ", " + msgBody);
                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }

    private void checkIfThisMessageIsCode(Context context, String msgBody, String fromNumber) {
        if (msgBody.contains("code")) {
            Toast.makeText(context, "Is my code", Toast.LENGTH_LONG).show();


            String[] split = msgBody.split("");
            String firstSubString = split[0];
            String secondSubString = split[1].trim();

            if (secondSubString.equals("vibrate")) {
                startVibrate(context);
            } else if (secondSubString.equals("sound")) {
                playSound(context);
            } else if (secondSubString.equals("sound off")) {
                stopSound(context);
            } else if (secondSubString.equals("location")) {
                getLocation(fromNumber, context);
            }
        }
    }

    private void stopSound(Context context) {
        if (mp != null) {
            mp.pause();
            mp.stop();
            mp.release();
            mp=null;
        }
    }

    private void getLocation(String fromNumber, Context context) {
        Location location = SharedPreferencesHelper.getInstance(context).getLastLocation();

        String msg = "My last location is:\n" + "http://maps.apple.com/?q=loc:" + location.getLatitude() + "+" + location.getLongitude();

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(fromNumber, null, msg, null, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static MediaPlayer mp;

    private void playSound(Context context) {
        mp = MediaPlayer.create(context, R.raw.ttt);
        mp.start();
    }

    private void startVibrate(Context context) {
        //  { delay, vibrate, sleep, vibrate, sleep } pattern
        long pattern[] = {0, 800, 200, 1200, 300, 2000, 400, 4000};
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));

        } else {
            v.vibrate(pattern, -1);
        }
    }
}