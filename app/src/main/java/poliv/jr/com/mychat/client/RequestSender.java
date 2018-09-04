package poliv.jr.com.mychat.client;

import android.os.Handler;

import communication.DC;
import communication.DataCarrier;

public class RequestSender extends ClientConnectionManager {

    private static RequestSender instance = null;


    public final static RequestSender getInstance(){
        if(instance == null)
            nNI.showToast("Attempting to connect to Server");
        return instance;
    }

    public final static RequestSender getInstance(NetworkNotificationInterface nNi, Handler handler){
        if (instance == null){
            RequestSender.nNI = nNi;
            instance = new RequestSender(/*handler*/);
        }

        return instance;
    }

    private RequestSender(){
        super();
    }

    private RequestSender(Handler handler){
        super(handler);
    }

    public DataCarrier loginUser(String userName, String password){
        DataCarrier<String[]> dataCarrier = new DataCarrier<>(DC.LOGIN_USER, new String[]{userName, password}, true);

        return sendRequest(dataCarrier);

    }

    public DataCarrier registerUser(String userName, String password){
        DataCarrier<String[]> dataCarrier = new DataCarrier<>(DC.REGISTER_USER, new String[]{userName, password}, true);

        return sendRequest(dataCarrier);
    }


}
