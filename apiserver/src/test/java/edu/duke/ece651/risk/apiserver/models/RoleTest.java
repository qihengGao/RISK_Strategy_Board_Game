package edu.duke.ece651.risk.apiserver.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleTest {
    @Test
    public void test_setGet(){
        Role role = new Role();
        role = new Role(ERole.ROLE_ADMIN);
        role.setId(0);
        assertEquals(0, role.getId());

        role.setName(ERole.ROLE_USER);
        assertEquals(ERole.ROLE_USER, role.getName());
    }
}
