package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {

    private String userName;
    private String password;
    private boolean online = false;
    private Map<String, Boolean> contacts;

    public User(){
        this("","");
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        contacts = new HashMap<>();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Map<String, Boolean> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, Boolean> contacts) {
        this.contacts = contacts;
    }

    public void addToContacts(String userName){
        addToContacts(userName, false);
    }

    public void addToContacts(String userName, boolean online){
        if( !contacts.containsKey(userName))
            contacts.put(userName, online);
    }

    public void removeFromContacts(String userName){
        if( contacts.containsKey(userName))
            contacts.remove(userName);
    }
}
