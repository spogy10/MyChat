package model;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {

    private String userName;
    private String password;
    private boolean online = false;
    private List<String> contacts;

    public User(){
        userName = "";
        password = "";
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
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

    public List getContacts() {
        return contacts;
    }

    public void setContacts(List contacts) {
        this.contacts = contacts;
    }

    public void addToContacts(String userName){
        if( !contacts.contains(userName))
            contacts.add(userName);
    }

    public void removeFromContacts(String userName){
        if( contacts.contains(userName))
            contacts.remove(userName);
    }
}
