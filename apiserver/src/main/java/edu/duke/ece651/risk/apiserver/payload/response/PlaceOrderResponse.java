package edu.duke.ece651.risk.apiserver.payload.response;

public class PlaceOrderResponse {
    private String prompt;

    /**
     * get prompt
     * @return prompt
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * set prompt
     * @param prompt
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
