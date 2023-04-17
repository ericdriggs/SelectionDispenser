package io.github.ericdriggs.selectiondispenser.dispenser;

import io.github.ericdriggs.selectiondispenser.SelfRefillingSelectionDispenser;
import io.github.ericdriggs.selectiondispenser.dispenser.crayon.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by edriggs on 10/11/15.
 */
public class SelfRefillingSelectionDispenserFunctionalTest {

    @BeforeEach
    void logTestName(TestInfo testInfo) {
        String methodName = testInfo.getTestMethod().orElseThrow().getName();
        System.out.println("Test " + getClass().getSimpleName() + "::" + methodName);
    }


    public SelfRefillingSelectionDispenser<Crayon, CrayonColor> getCrayonDispenser() {
        return new SelfRefillingSelectionDispenser<Crayon, CrayonColor>(new CrayonFactory());
    }

    @Test
    public void whenInstantiateSetsDefaultInitialInventoryTest() {
        SelfRefillingSelectionDispenser<Crayon, CrayonColor> dispenser = getCrayonDispenser();
        for (CrayonColor selection : dispenser.getSelections()) {
            assertEquals(dispenser.getSelectionInventoryCount(selection), 1);
        }
    }

    @Test
    public void whenInstantiateSetsDesiredInventoryTest() throws InterruptedException {
        final int expectedBlueCount = 3;
        final int expectedGreenCount = 2;
        final int expectedRedCount = 0;

        Map<CrayonColor, Integer> desiredInventory = new HashMap<CrayonColor, Integer>();
        desiredInventory.put(CrayonColor.BLUE, expectedBlueCount);
        desiredInventory.put(CrayonColor.GREEN, expectedGreenCount);
        SelfRefillingSelectionDispenser<Crayon, CrayonColor> dispenser =
                new SelfRefillingSelectionDispenser<Crayon, CrayonColor>(new CrayonFactory());
        dispenser.setDesiredInventory(desiredInventory);
        Thread.sleep(100);

        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.BLUE), expectedBlueCount);
        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.GREEN), expectedGreenCount);
        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.RED), expectedRedCount);
    }

    @Test
    public void whenHasInventoryForAllSelectionAfterCreateTest() {
        SelfRefillingSelectionDispenser<Crayon, CrayonColor> dispenser = getCrayonDispenser();
        for (CrayonColor selection : dispenser.getSelections()) {
            assertEquals(dispenser.getSelectionInventoryCount(selection), 1);
        }
    }

    @Test
    public void hasMoreItemsAfterRefillTest() {
        SelfRefillingSelectionDispenser<Crayon, CrayonColor> dispenser = getCrayonDispenser();
        dispenser.addInventory(CrayonColor.BLUE, new Crayon(CrayonColor.BLUE));
        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.BLUE), 1);
    }

    @Test
    public void inventoryDecreasesAfterDispenseTest() {
        SelfRefillingSelectionDispenser<Crayon, CrayonColor> dispenser = getCrayonDispenser();
        dispenser.addInventory(CrayonColor.BLUE, new Crayon(CrayonColor.BLUE));
        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.BLUE), 1);
        Crayon crayon = dispenser.dispense(CrayonColor.BLUE);
        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.BLUE), 0);
    }

    @Test
    public void whenEmptyDispenseNewTest() {
        SelfRefillingSelectionDispenser<Crayon, CrayonColor> dispenser = getCrayonDispenser();
        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.BLUE), 0);
        Crayon crayon = dispenser.dispense(CrayonColor.BLUE);
        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.BLUE), 0);
    }

    @Test
    public void createInventoryTest() throws InterruptedException {
        SelfRefillingSelectionDispenser<Crayon, CrayonColor> dispenser = getCrayonDispenser();
        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.BLUE), 0);
        dispenser.createInventory(CrayonColor.BLUE, 3);
        Thread.sleep(20);
        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.BLUE), 3);
    }
}
