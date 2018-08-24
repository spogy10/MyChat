package model;

import java.io.Serializable;

public class Message implements Serializable {

    private String sender = "";
    private String recipient = "";
    private String message = "";


    public Message(String recipient, String message) {
        this.recipient = recipient;
        this.message = message;
    }

    public Message(String sender, String recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

