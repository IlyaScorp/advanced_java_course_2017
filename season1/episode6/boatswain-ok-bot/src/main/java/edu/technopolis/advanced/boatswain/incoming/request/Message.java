package edu.technopolis.advanced.boatswain.incoming.request;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    private String seq;
    private String mid;
    private String text;

    public Message() {
    }

    public Message(String text) {
        this.text = text;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
                "seq='" + seq + '\'' +
                ", mid='" + mid + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
