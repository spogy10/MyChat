package poliv.jr.com.mychat.client;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import communication.DC;
import communication.DataCarrier;
import model.Message;
import poliv.jr.com.mychat.MyChat;
import poliv.jr.com.mychat.client.listeners.ContactAddedListener;
import poliv.jr.com.mychat.client.listeners.ContactListListener;
import poliv.jr.com.mychat.client.listeners.ContactOnlineListener;
import poliv.jr.com.mychat.client.listeners.MessageNotification;


public class ClientConnectionManager extends Client implements Runnable {
    private static final String HOST = "10.0.2.2";
    private static final int PORT = 4000;
    private Thread t;
    protected static NetworkNotificationInterface nNI;

    private String action = "";
    private DataCarrier carrier = null;
    private DataCarrier response = null;
    private ContactAddedListener contactAddedListener = null;
    private ContactOnlineListener contactOnlineListener = null;
    private MessageNotification messageNotificationListener = null;

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

    public void setContactListListener(ContactListListener listener){
        contactOnlineListener = listener;
        contactAddedListener = listener;
        messageNotificationListener = listener;
    }

    public void removerContactListListener(){
        contactOnlineListener = null;
        contactAddedListener = null;
        messageNotificationListener = null;
    }

    public void removeContactAddedListener() {
        contactAddedListener = null;
    }

    public void setContactAddedListener(ContactAddedListener contactAddedListener) {
        this.contactAddedListener = contactAddedListener;
    }

    public void removeContactOnlineListener() {
        contactOnlineListener = null;
    }

    public void setContactOnlineListener(ContactOnlineListener contactOnlineListener) {
        this.contactOnlineListener = contactOnlineListener;
    }

    public void removeMessageNotificationListener() {
        messageNotificationListener = null;
    }

    public void setMessageNotificationListener(MessageNotification messageNotificationListener) {
        this.messageNotificationListener = messageNotificationListener;
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
        if(os == null || is == null) {
            nNI.showToast("Not connected to server, please restart app");
            return response;
        }
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
        receiveRequest(action);
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

            case DC.ADD_CONTACT:
                addContact();
                break;

            case DC.REMOVE_CONTACT:
                removeContact();
                break;
        }
    }

    private void contactOffline() {
        String userName = (String) carrier.getData();
        MyChat.myUser.getContacts().put(userName, false);
        Log.d("Paul", userName+" is not online");
        if(contactOnlineListener != null)
            contactOnlineListener.onContactOffline(userName);

    }

    private void contactOnline() {
        String userName = (String) carrier.getData();
        MyChat.myUser.getContacts().put(userName, true);
        Log.d("Paul", userName+" is now online");
        if(contactOnlineListener != null)
            contactOnlineListener.onContactOnline(userName);
    }

    private void addContact(){
        Object[] contact = (Object[]) carrier.getData();
        String userName = (String) contact[0];
        Boolean online = (Boolean) contact[1];
        MyChat.myUser.getContacts().put(userName, online);
        Log.d("Paul", userName+" is now added and "+online);
        if(contactAddedListener != null)
            contactAddedListener.onContactAdded(userName);
    }

    private void removeContact(){
        String userName = (String) carrier.getData();
        MyChat.myUser.getContacts().remove(userName);
        Log.d("Paul", userName+" is now removed");
        if(contactAddedListener != null)
            contactAddedListener.onContactRemoved(userName);
    }

    private void receiveRequest(String request){
        Log.d("Paul","Request: "+request+" received");
    }

    private void receiveMessage() {
        Message receivedMessage = (Message) carrier.getData();
        Log.d("Paul", "Received a message from "+receivedMessage.getSender());
        if(messageNotificationListener != null)
            messageNotificationListener.messageReceived(receivedMessage);
    }

    public static void setnNI(NetworkNotificationInterface nNI) {
        ClientConnectionManager.nNI = nNI;
    }
}
