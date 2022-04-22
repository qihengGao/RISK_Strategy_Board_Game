package edu.duke.ece651.risk.apiserver.payload.request;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginRequestTest {
    @Test
    void allTests() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword("12345");
        assertEquals(loginRequest.getPassword(), "12345");
        loginRequest.setUsername("user");
        assertEquals(loginRequest.getUsername(), "user");
    }
}