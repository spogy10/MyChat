package poliv.jr.com.mychat;

import android.app.Application;
import android.util.Log;

import model.User;
import poliv.jr.com.mychat.client.RequestSender;

public class MyChat extends Application {

    public static User myUser = new User();
    private static Thread clientThread;

    @Override
    public void onCreate() {
        super.onCreate();
        startClientThread();
    }

    private void startClientThread() {
        clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Paul", "thread started");
                RequestSender r = RequestSender.getInstance();
            }
        });

        clientThread.start();
    }
}
