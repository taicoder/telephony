package com.android.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MyCallRecevier extends BroadcastReceiver {
    ArrayList<String> listNumber;
    private ITelephony telephonyService;
    String phoneNumber;
    @Override
    public void onReceive(Context context, Intent intent) {
        listNumber = new ArrayList<>();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while(cursor.moveToNext()){
            phoneNumber =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            listNumber.add(phoneNumber);
        }

        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)){
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
           if(!listNumber.contains(incomingNumber)){
               TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
               try {
                   Class c = Class.forName(tm.getClass().getName());
                   Method m = c.getDeclaredMethod("getITelephony");
                   m.setAccessible(true);
                   ITelephony telephonyService = (ITelephony) m.invoke(tm);
//            Bundle bundle = intent.getExtras();
//            String phoneNumber = bundle.getString("incoming_number");
                  // if ((phoneNumber != null)) {
                       //   telephonyService.silenceRinger();
                       telephonyService.endCall();
                 //  }
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
        }

    }


}
