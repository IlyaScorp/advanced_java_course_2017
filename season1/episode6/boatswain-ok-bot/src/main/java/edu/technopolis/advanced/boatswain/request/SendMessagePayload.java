package edu.technopolis.advanced.boatswain.request;

import edu.technopolis.advanced.boatswain.incoming.request.Message;

public class SendMessagePayload implements RequestPayload {
    private final SendRecipient recipient;
    private final Message message;

    public SendMessagePayload(SendRecipient recipient, Message message) {
        this.recipient = recipient;
        this.message = message;
    }

    public SendRecipient getRecipient() {
        return recipient;
    }

    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "SendMessagePayload{" +
                "recipient=" + recipient +
                ", message=" + message +
                '}';
    }
}
