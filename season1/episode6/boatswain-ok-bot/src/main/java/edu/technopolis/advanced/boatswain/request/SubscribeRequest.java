package edu.technopolis.advanced.boatswain.request;

public class SubscribeRequest extends Request<SubscribePayload, SubscribeRequest> {
    private final String query;

    public SubscribeRequest(String subscriptionEndpoint) {
        this(subscriptionEndpoint, null);
    }

    public SubscribeRequest(String subscriptionEndpoint, SubscribePayload payload) {
        super(payload);
        this.query = subscriptionEndpoint;
    }

    @Override
    protected SubscribeRequest thiss() {
        return this;
    }

    @Override
    public String getQueryStart() {
        return query;
    }
}
