package edu.duke.ece651.risk.apiserver.payload.response;

public class placeUnitResponse {

    private String prompt;

    public placeUnitResponse(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
