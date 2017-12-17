package edu.technopolis.advanced.boatswain.request;

public class SendMessageRequest extends Request<SendMessagePayload, SendMessageRequest> {
    private final String query;
    private final String chatId;

    public SendMessageRequest(String queryString, String chatId) {
        this.query = queryString;
        this.chatId = chatId;
    }

    @Override
    protected SendMessageRequest thiss() {
        return this;
    }

    @Override
    public String getQueryStart() {
        return query + '/' + chatId;
    }

    @Override
    public String toString() {
        return "SendMessageRequest{" +
                "query='" + getQueryStart() + '\'' +
                ", payload='" + getPayload() + '\'' +
                '}';
    }
}
