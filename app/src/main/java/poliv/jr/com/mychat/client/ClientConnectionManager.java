package poliv.jr.com.mychat.client;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import communication.DC;
import communication.DataCarrier;


public class ClientConnectionManager extends Client implements Runnable {
    private static final String HOST = "10.0.2.2";
    private static final int PORT = 4000;
    private Thread t;
    protected static NetworkNotificationInterface nNI;

    private String action = "";
    private DataCarrier carrier = null;
    private DataCarrier response = null;

    DataCarrier tempResponseHolder;
    AtomicBoolean unreadResponse = new AtomicBoolean(false);


    protected ClientConnectionManager(){
        super(HOST, PORT);
        if(os != null && is != null){
            t = new Thread(this);
            t.start();
        }else{
            Log.d("Paul", "Streams are null");
            nNI.showToast("Not connected to server");
        }

    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return is.readObject();
    }

    public boolean writeObject(Object obj){
        boolean success = false;
        try {
            os.writeObject(obj);
            success = true;
        }catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }

    public static boolean responseCheck(DataCarrier response) {
        if(!response.isRequest()) {
            if (response.getInfo().equals(DC.NO_ERROR)) {
                Log.d("Paul", "NO ERROR");
                return true;
            }

            if (response.getInfo().equals(DC.SERVER_CONNECTION_ERROR)) {
                Log.d("Paul", "SERVER CONNECTION ERROR");
                //JOptionPane.showMessageDialog(null, "Server Connection Error, Please Restart Server");
            }

            if (response.getInfo().equals(DC.GENERAL_ERROR)) {
                Log.d("Paul", "GENERAL ERROR");
                //JOptionPane.showMessageDialog(null, "An Error Occurred");
            }

        }else{
            Log.d("Paul", "Response set as request");
        }
        return false;
    }

    protected DataCarrier sendRequest(DataCarrier request, boolean responseRequired){
        DataCarrier response = new DataCarrier(DC.SERVER_CONNECTION_ERROR, false);
        try{
            os.writeObject(request);
            Log.d("Paul", "Request: "+request.getInfo()+" sent");
            if(responseRequired) {
                response = waitForResponse();
                Log.d("Paul", "Response for "+request.getInfo()+" received");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }

    protected DataCarrier sendRequest(DataCarrier request){
        DataCarrier response = new DataCarrier(DC.SERVER_CONNECTION_ERROR, false);
        if(os == null || is == null) {
            nNI.showToast("Not connected to server, please restart app");
            return response;
        }
        try{
            os.writeObject(request);
            Log.d("Paul", "Request: "+request.getInfo()+" sent");
            response = waitForResponse();
            Log.d("Paul", "Response for "+request.getInfo()+" received");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    private DataCarrier waitForResponse() {
        while(!unreadResponse.get()){
            /*wait until response comes in*/
        }

        unreadResponse.compareAndSet(true, false);
        return tempResponseHolder;
    }

    @Override
    public void run() {
        action = "";
        try{
            while(!action.equals(DC.DISCONNECT)){
                carrier = (DataCarrier) is.readObject();
                if(carrier.isRequest()){
                    action = carrier.getInfo();
                    response = new DataCarrier(DC.NO_ERROR, false);
                    caseStatements();
                }else {
                    tempResponseHolder = carrier;
                    unreadResponse.compareAndSet(false, true);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void caseStatements() {
        switch (action){ //requests from server such as getting message
            case DC.RECEIVE_MESSAGE:
                receiveMessage();
                break;

            case DC.CONTACT_ONLINE:
                contactOnline();
                break;

            case DC.CONTACT_OFFLINE:
                contactOffline();
                break;
        }
    }

    private void contactOffline() {
        receiveRequest(action);

    }

    private void contactOnline() {
        receiveRequest(action);
    }

    private void receiveRequest(String request){
        System.out.println("Request: "+request+" received");
    }

    private void receiveMessage() {
        receiveRequest(action);
    }

    public static void setnNI(NetworkNotificationInterface nNI) {
        ClientConnectionManager.nNI = nNI;
    }
}
