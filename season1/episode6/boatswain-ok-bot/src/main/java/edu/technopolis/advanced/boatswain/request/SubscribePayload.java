package edu.technopolis.advanced.boatswain.request;

public class SubscribePayload implements RequestPayload {
    private String url;
    private String filter;

    public SubscribePayload() {
    }

    public SubscribePayload(String url, String filter) {
        this.url = url;
        this.filter = filter;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    @Override
    public String toString() {
        return "SubscribePayload{" +
                "url='" + url + '\'' +
                ", filter='" + filter + '\'' +
                '}';
    }
}
