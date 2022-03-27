package edu.duke.ece651.risk.apiserver.payload.response;

public class PlaceUnitResponse {

    private String prompt;

    public PlaceUnitResponse(String prompt) {
        this.prompt = prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }
}
