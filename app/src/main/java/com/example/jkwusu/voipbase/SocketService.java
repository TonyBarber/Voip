package com.example.jkwusu.voipbase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by jkwusu on 2016/1/17.
 */
public class SocketService extends Service {

    public static Socket socket;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket=new Socket("192.168.191.1", 8000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("service start", "service start");
            }
        }).start();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("service finish", "service finish");
    }
}
