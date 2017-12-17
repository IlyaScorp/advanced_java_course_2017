package edu.technopolis.advanced.boatswain.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class SendRecipient {
    private String userId;

    public SendRecipient() {
    }

    public SendRecipient(String userId) {
        this.userId = userId;
    }

    @JsonGetter("user_id")
    public String getUserId() {
        return userId;
    }

    @JsonSetter("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
