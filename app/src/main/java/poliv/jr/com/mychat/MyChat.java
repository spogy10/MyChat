package poliv.jr.com.mychat;

import android.app.Application;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import model.User;
import poliv.jr.com.mychat.client.NetworkNotificationInterface;
import poliv.jr.com.mychat.client.RequestSender;

public class MyChat extends Application implements NetworkNotificationInterface {

    public static User myUser = new User();
    private static Thread clientThread;
    private Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(getMainLooper());
        startClientThread();
    }

    private void startClientThread() {
        clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("Paul", "network thread started");
                RequestSender.getInstance(MyChat.this);
            }
        });

        clientThread.start();
    }

    @Override
    public void showToast(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyChat.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

}
