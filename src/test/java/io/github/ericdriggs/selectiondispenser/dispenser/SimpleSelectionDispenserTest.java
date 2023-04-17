package io.github.ericdriggs.selectiondispenser.dispenser;

import io.github.ericdriggs.selectiondispenser.SimpleSelectionDispenser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.github.ericdriggs.selectiondispenser.dispenser.crayon.Crayon;
import io.github.ericdriggs.selectiondispenser.dispenser.crayon.CrayonColor;
import org.junit.jupiter.api.TestInfo;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Created by edriggs on 10/11/15.
 */
public class SimpleSelectionDispenserTest {

    @BeforeEach
    void logTestName(TestInfo testInfo) {
        String methodName = testInfo.getTestMethod().orElseThrow().getName();
        System.out.println("Test " + getClass().getSimpleName() + "::" + methodName);
    }

    public SimpleSelectionDispenser<Crayon, CrayonColor> getCrayonDispenser() {
        return new SimpleSelectionDispenser<Crayon, CrayonColor>();
    }

    @Test
    public void selectionInventoryCountZeroAfterCreateTest() {
        SimpleSelectionDispenser<Crayon, CrayonColor> dispenser = getCrayonDispenser();
        for ( CrayonColor crayonColor : EnumSet.allOf(CrayonColor.class)) {
            assertEquals(dispenser.getSelectionInventoryCount(crayonColor), 0);
        }
    }

    @Test
    public void hasItemsAfterRefillTest() {
        SimpleSelectionDispenser<Crayon, CrayonColor> dispenser = getCrayonDispenser();
        dispenser.addInventory(CrayonColor.BLUE, new Crayon(CrayonColor.BLUE));
        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.BLUE), 1);
    }

    @Test
    public void inventoryDecreasesAfterDispenseTest() {
        SimpleSelectionDispenser<Crayon, CrayonColor> dispenser = getCrayonDispenser();
        dispenser.addInventory(CrayonColor.BLUE, new Crayon(CrayonColor.BLUE));
        Crayon crayon = dispenser.dispense(CrayonColor.BLUE);
        assertNotNull(crayon);
        assertEquals(crayon.getCrayonColor(), CrayonColor.BLUE);
        assertEquals(dispenser.getSelectionInventoryCount(CrayonColor.BLUE), 0);
    }
}
