package edu.duke.ece651.risk.apiserver.payload.request;

public class Message {

    private String from;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String text;

    // adding getters and setters here
}