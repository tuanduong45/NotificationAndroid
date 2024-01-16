package hust.edu.vn.smsdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Bundle bundle = intent.getExtras();
        Object[] smsObj = (Object[]) bundle.get("pdus");
        String strMessage = "";
        String format = bundle.getString("format");
        for(Object obj : smsObj){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
            String mobNo = smsMessage.getDisplayOriginatingAddress();
            String msg = smsMessage.getDisplayMessageBody();
            Log.d("Msg: ","MsbNo" + mobNo + ", Msg: " + msg);
        }

        throw new UnsupportedOperationException("Not yet implemented");
    }
}