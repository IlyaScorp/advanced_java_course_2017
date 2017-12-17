package edu.technopolis.advanced.boatswain.incoming.request;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class Recipient {
    private String chatId;

    @JsonGetter("chat_id")
    public String getChatId() {
        return chatId;
    }

    @JsonSetter("chat_id")
    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "Recipient{" +
                "chatId='" + chatId + '\'' +
                '}';
    }
}
