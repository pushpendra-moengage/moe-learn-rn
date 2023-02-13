package com.appinjs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.moengage.plugin.base.push.PluginPushCallback;

public class MyPluginPushCallback extends PluginPushCallback {
    public MyPluginPushCallback() {
        super();
    }

    @Override
    public void handleCustomAction(@NonNull Context context, @NonNull String payload) {
        super.handleCustomAction(context, payload);
    }

    @Override
    public void onNotificationClick(@NonNull Activity activity, @NonNull Bundle payload) {
        Log.e("MoE my Push Click", payload.toString());
        super.onNotificationClick(activity, payload);
    }
}
