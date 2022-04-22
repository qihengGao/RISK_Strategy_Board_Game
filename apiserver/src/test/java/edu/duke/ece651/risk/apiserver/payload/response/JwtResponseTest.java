package edu.duke.ece651.risk.apiserver.payload.response;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JwtResponseTest {

    @Test
    public void test_setGet(){
        List<String> roles = new LinkedList<>();
        roles.add("admin");
        roles.add("user");

        JwtResponse response = new JwtResponse("", 0L, "", "", roles,30L);
        response.setAccessToken("token");
        assertEquals("token", response.getAccessToken());

        response.setTokenType("tokenType");
        assertEquals("tokenType", response.getTokenType());

        response.setId(1L);
        assertEquals(1L, response.getId());

        response.setEmail("email");
        assertEquals("email", response.getEmail());

        response.setUsername("username");
        assertEquals("username", response.getUsername());

        assertEquals(response.getElo(), 30L);
        response.setElo(100L);
        assertEquals(response.getElo(), 100L);

        assertEquals(roles, response.getRoles());
    }
}
