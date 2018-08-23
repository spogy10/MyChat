package poliv.jr.com.mychat.client;

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


}
