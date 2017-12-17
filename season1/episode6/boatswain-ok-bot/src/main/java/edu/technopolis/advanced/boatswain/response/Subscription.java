package edu.technopolis.advanced.boatswain.response;

public class Subscription {
    private long time;
    private String url;
    private String filter;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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
        return "Subscription{" +
                "time=" + time +
                ", url='" + url + '\'' +
                ", filter='" + filter + '\'' +
                '}';
    }
}
