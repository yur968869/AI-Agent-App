package com.example.aiagent;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

public class PythonServerService extends Service {
    private static final String TAG = "PythonServerService";

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the ActionHelper with the app context
        ActionHelper.init(this);          // <-- Add this line
        startPythonServer();
    }

    private void startPythonServer() {
        new Thread(() -> {
            if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(this));
            }
            Python py = Python.getInstance();
            py.getModule("ai_agent_server").callAttr("main");
        }).start();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
