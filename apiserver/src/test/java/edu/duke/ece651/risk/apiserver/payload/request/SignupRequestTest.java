package edu.duke.ece651.risk.apiserver.payload.request;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignupRequestTest {
    @Test
    public void test_getSet(){
        SignupRequest request = new SignupRequest();

        request.setUsername("username");
        assertEquals("username", request.getUsername());

        request.setEmail("email");
        assertEquals("email", request.getEmail());

        request.setPassword("password");
        assertEquals("password", request.getPassword());

        Set<String> roles = new HashSet<>();
        roles.add("role");
        request.setRole(roles);
        assertEquals(roles, request.getRole());
    }
}
