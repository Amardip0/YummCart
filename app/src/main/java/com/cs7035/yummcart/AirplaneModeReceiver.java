package com.cs7035.yummcart;

import android.content.*;
import android.provider.Settings;
import android.widget.Toast;

public class AirplaneModeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context c, Intent i) {
        if (Intent.ACTION_AIRPLANE_MODE_CHANGED.equals(i.getAction()) || "android.intent.action.AIRPLANE_MODE".equals(i.getAction())) {
            boolean state = i.getBooleanExtra("state", false);
            if (!i.hasExtra("state")) {
                state = Settings.Global.getInt(c.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
            }
            Toast.makeText(c, state ? "Airplane Mode Enabled" : "Airplane Mode Disabled", Toast.LENGTH_LONG).show();
        }
    }
}
