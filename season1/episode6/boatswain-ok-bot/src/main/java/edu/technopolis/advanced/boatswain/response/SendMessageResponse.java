package edu.technopolis.advanced.boatswain.response;

import com.fasterxml.jackson.annotation.JsonSetter;

public class SendMessageResponse extends Response {
    String recipientId;
    String messageId;

    public String getRecipientId() {
        return recipientId;
    }

    @JsonSetter("recipient_id")
    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getMessageId() {
        return messageId;
    }

    @JsonSetter("message_id")
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String toString() {
        return "SendMessageResponse{" +
                "recipientId='" + recipientId + '\'' +
                ", messageId='" + messageId + '\'' +
                '}';
    }
}
