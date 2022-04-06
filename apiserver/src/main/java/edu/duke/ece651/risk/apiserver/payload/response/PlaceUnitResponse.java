package edu.duke.ece651.risk.apiserver.payload.response;

public class PlaceUnitResponse {
    private String prompt;

    /**
     * default constructor
     * @param prompt
     */
    public PlaceUnitResponse(String prompt) {
        this.prompt = prompt;
    }

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
