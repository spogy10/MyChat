package poliv.jr.com.mychat.contactlist.listeners;

public interface ContactOnlineListener {
    void onContactOnline(String contactName);

    void onContactOffline(String contactName);
}
