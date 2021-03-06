package edu.duke.ece651.risk.apiserver.models;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void test_setGet(){
        User user = new User();
        assertEquals(user.getElo(), 1000L);

        user = new User("", "", "", 50L);

        user.setId(0L);
        assertEquals(0L, user.getId());

        user.setUsername("username");
        assertEquals("username", user.getUsername());

        user.setEmail("email");
        assertEquals("email", user.getEmail());

        user.setPassword("password");
        assertEquals("password", user.getPassword());

        assertEquals(user.getElo(), 50L);

        user.setElo(0L);
        assertEquals(user.getElo(), 0L);

        Set<Role> roles = new HashSet<>();
        roles.add(new Role());
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());
    }
}
