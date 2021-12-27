package com.example.bookmanager_java;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyService3 extends Service {
    @Override
    public void onCreate(){
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    Intent intent = new Intent(MyService3.this,MainActivity.class);

                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyService3.this.startActivity(intent);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }).start();

        stopSelf();
    }
    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        super.onStartCommand(intent,flags,startId);
        return START_STICKY;
    }


    public void onDestory(){
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}