package edu.technopolis.advanced.boatswain.request;

public class GetSubscriptionsRequest extends Request<RequestPayload, GetSubscriptionsRequest> {
    private final String query;

    public GetSubscriptionsRequest(String query) {
        this.query = query;
    }

    @Override
    public String getQueryStart() {
        return query;
    }

    @Override
    protected GetSubscriptionsRequest thiss() {
        return this;
    }
}
