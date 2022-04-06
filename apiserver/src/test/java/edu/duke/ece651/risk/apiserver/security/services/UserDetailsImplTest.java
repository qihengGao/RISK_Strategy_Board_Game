package edu.duke.ece651.risk.apiserver.security.services;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDetailsImplTest {

    @Test
    public void test_getSet(){
        UserDetailsImpl userDetails = new UserDetailsImpl(
                0L, "username", "email", "password", null);
        assertEquals(null, userDetails.getAuthorities());
        assertEquals(0L, userDetails.getId());
        assertEquals("username", userDetails.getUsername());
        assertEquals("email", userDetails.getEmail());
        assertEquals("password", userDetails.getPassword());

        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
    }
}
