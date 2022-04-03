package edu.duke.ece651.risk.shared;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OwnerTest {
    @Test
    public void test_defaultConstructor_success(){
        Owner owner = new Owner(0L, 2, 10, 20);
        assertEquals(0L, owner.getOwnerId());
        assertEquals(2, owner.getCurrTechlevel());
        assertEquals(10, owner.getOwnedFoodResource());
        assertEquals(20, owner.getOwnedTechResource());
    }

    @Test
    public void test_defaultConstructor_fail() {
        assertThrows(IllegalArgumentException.class, ()->new Owner(0L, -1, 10, 20));
        assertThrows(IllegalArgumentException.class, ()->new Owner(0L, 7, 10, 20));
        assertThrows(IllegalArgumentException.class, ()->new Owner(0L, 3, -1, 20));
        assertThrows(IllegalArgumentException.class, ()->new Owner(0L, 3, 10, -1));
    }

    @Test
    public void test_initializerConstructor(){
        Owner owner = new Owner(0L);
        assertEquals(0L, owner.getOwnerId());
        assertEquals(1, owner.getCurrTechlevel());
        assertEquals(0, owner.getOwnedFoodResource());
        assertEquals(0, owner.getOwnedTechResource());
    }

    @Test
    public void test_tryAddOrRemoveFoodResource(){
        Owner owner = new Owner(0L, 1, 0, 0);

        // success
        assertNull(owner.tryAddOrRemoveFoodResource(10));
        assertEquals(10, owner.getOwnedFoodResource());
        assertNull(owner.tryAddOrRemoveFoodResource(-8));
        assertEquals(2, owner.getOwnedFoodResource());

        // error: reduce below 0
        String prompt = "You do not have sufficient food resources";
        assertEquals(prompt, owner.tryAddOrRemoveFoodResource(-3));
    }

    @Test
    public void test_tryAddOrRemoveTechResource() {
        Owner owner = new Owner(0L, 1, 0, 0);

        // success
        assertNull(owner.tryAddOrRemoveTechResource(10));
        assertEquals(10, owner.getOwnedTechResource());
        assertNull(owner.tryAddOrRemoveTechResource(-8));
        assertEquals(2, owner.getOwnedTechResource());

        // error: reduce below 0
        String prompt = "You do not have sufficient tech resources";
        assertEquals(prompt, owner.tryAddOrRemoveTechResource(-3));
    }

    @Test
    public void test_tryUpgradeTechLevel(){
        // success
        Owner owner1 = new Owner(0L, 1, 0, 130);
        assertNull(owner1.tryUpgradeTechLevel());
        assertEquals(2, owner1.getCurrTechlevel());
        assertEquals(80, owner1.getOwnedTechResource());
        assertNull(owner1.tryUpgradeTechLevel());
        assertEquals(3, owner1.getCurrTechlevel());
        assertEquals(5, owner1.getOwnedTechResource());

        // error: no enough tech resource
        String prompt = "you do not have enough technological resource to upgrade";
        assertEquals(prompt, owner1.tryUpgradeTechLevel());
        assertEquals(3, owner1.getCurrTechlevel());

        // error: already at max level
        // success
        Owner owner2 = new Owner(0L, 6, 0, 10000);
        prompt = "you are already at maximum technology level, cannot upgrade";
        assertEquals(prompt, owner2.tryUpgradeTechLevel());
        assertEquals(6, owner2.getCurrTechlevel());
    }
}
