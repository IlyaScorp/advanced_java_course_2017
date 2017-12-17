package edu.technopolis.advanced.boatswain.incoming.request;

import com.fasterxml.jackson.annotation.JsonSetter;

public class Sender {
    private String name;
    private String userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    @JsonSetter("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Sender{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
