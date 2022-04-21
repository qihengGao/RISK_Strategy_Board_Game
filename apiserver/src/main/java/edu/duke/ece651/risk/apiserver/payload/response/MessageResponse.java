package edu.duke.ece651.risk.apiserver.payload.response;



public class MessageResponse {
    private String message;

    /**
     * default constructor
     * @param message
     */
    public MessageResponse(String message) {
        this.message = message;
    }

    /**
     * get message
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * set message
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}