package edu.technopolis.advanced.boatswain.request;

public abstract class Request<RP extends RequestPayload, R extends Request<RP, R>> {
    private RP payload;

    public Request() {
    }

    public Request(RP payload) {
        this.payload = payload;
    }

    public RP getPayload() {
        return payload;
    }

    public R setPayload(RP payload) {
        this.payload = payload;
        return thiss();
    }

    protected abstract R thiss();

    public abstract String getQueryStart();
}
