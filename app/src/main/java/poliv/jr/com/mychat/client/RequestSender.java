package poliv.jr.com.mychat.client;

import communication.DC;
import communication.DataCarrier;

public class RequestSender extends ClientConnectionManager {

    private static RequestSender instance = null;


    public final static RequestSender getInstance(){
        if (instance == null)
            instance = new RequestSender();

        return instance;
    }

    private RequestSender(){
        super();
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
