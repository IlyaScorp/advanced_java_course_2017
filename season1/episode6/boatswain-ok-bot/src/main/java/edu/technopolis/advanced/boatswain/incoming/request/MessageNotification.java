package edu.technopolis.advanced.boatswain.incoming.request;

import java.io.Serializable;

public class MessageNotification implements Serializable {
    private Sender sender;
    private Recipient recipient;
    private Message message;
    private long timestamp;

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageNotification{" +
                "sender=" + sender +
                ", recipient=" + recipient +
                ", message=" + message +
                ", timestamp=" + timestamp +
                '}';
    }
}
