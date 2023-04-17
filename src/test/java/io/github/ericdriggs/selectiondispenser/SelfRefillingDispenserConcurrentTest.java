package io.github.ericdriggs.selectiondispenser;

import io.github.ericdriggs.selectiondispenser.dispenser.turnstyle.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.reflect.Method;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by edriggs on 10/18/15.
 */
public class SelfRefillingDispenserConcurrentTest {

    ExecutorService executorService = Executors.newCachedThreadPool();

    @BeforeEach
    void logTestName(TestInfo testInfo) {
        String methodName = testInfo.getTestMethod().orElseThrow().getName();
        System.out.println("Test " + getClass().getSimpleName() + "::" + methodName);
    }

    @Test
    public void fifoNoInventoryRequestsTest() throws InterruptedException {

        TurnstileEventFactory turnstileEventFactory = new TurnstileEventFactory(100);

        SelfRefillingSelectionDispenser<TurnstileEvent, TurnstileLane> dispenser =
                new SelfRefillingSelectionDispenser<TurnstileEvent, TurnstileLane>(turnstileEventFactory);

        DispenseCallable callable1 = new DispenseCallable(TurnstileLane.ONE, dispenser);
        DispenseCallable callable2 = new DispenseCallable(TurnstileLane.TWO, dispenser);
        DispenseCallable callable3 = new DispenseCallable(TurnstileLane.THREE, dispenser);
        executorService.submit(callable1);
        executorService.submit(callable2);
        executorService.submit(callable3);

        assertFalse(callable1.isComplete);
        assertFalse(callable2.isComplete);
        assertFalse(callable3.isComplete);

        turnstileEventFactory.fireEvent(TurnstileLane.ONE);
        Thread.sleep(40);
        assertTrue(callable1.isComplete);

        turnstileEventFactory.fireEvent(TurnstileLane.TWO);
        Thread.sleep(40);
        assertTrue(callable2.isComplete);

        turnstileEventFactory.fireEvent(TurnstileLane.THREE);
        Thread.sleep(40);
        assertTrue(callable3.isComplete);
    }

    @Test
    public void fifoNoInventoryRequestsTest2() throws InterruptedException {

        TurnstileEventFactory turnstileEventFactory = new TurnstileEventFactory(100);

        SelfRefillingSelectionDispenser<TurnstileEvent, TurnstileLane> dispenser =
                new SelfRefillingSelectionDispenser<TurnstileEvent, TurnstileLane>(turnstileEventFactory);

        DispenseCallable callable1 = new DispenseCallable(TurnstileLane.ONE, dispenser);
        DispenseCallable callable2 = new DispenseCallable(TurnstileLane.TWO, dispenser);
        DispenseCallable callable3 = new DispenseCallable(TurnstileLane.THREE, dispenser);
        executorService.submit(callable1);
        executorService.submit(callable2);
        executorService.submit(callable3);

        assertFalse(callable1.isComplete);
        assertFalse(callable2.isComplete);
        assertFalse(callable3.isComplete);

        turnstileEventFactory.fireEvent(TurnstileLane.ONE);
        Thread.sleep(40);
        assertTrue(callable1.isComplete);

        turnstileEventFactory.fireEvent(TurnstileLane.TWO);
        Thread.sleep(40);
        assertTrue(callable2.isComplete);

        turnstileEventFactory.fireEvent(TurnstileLane.THREE);
        Thread.sleep(40);
        assertTrue(callable3.isComplete);
    }

    @Test
    public void lifoNoInventoryRequestsTest() throws InterruptedException {

        TurnstileEventFactory turnstileEventFactory = new TurnstileEventFactory(100);

        SelfRefillingSelectionDispenser<TurnstileEvent, TurnstileLane> dispenser =
                new SelfRefillingSelectionDispenser<TurnstileEvent, TurnstileLane>(turnstileEventFactory);

        DispenseCallable callable1 = new DispenseCallable(TurnstileLane.ONE, dispenser);
        DispenseCallable callable2 = new DispenseCallable(TurnstileLane.TWO, dispenser);
        DispenseCallable callable3 = new DispenseCallable(TurnstileLane.THREE, dispenser);

        executorService.submit(callable3);
        executorService.submit(callable2);
        executorService.submit(callable1);

        assertFalse(callable1.isComplete);
        assertFalse(callable2.isComplete);
        assertFalse(callable3.isComplete);

        turnstileEventFactory.fireEvent(TurnstileLane.ONE);
        Thread.sleep(40);
        assertTrue(callable1.isComplete);

        turnstileEventFactory.fireEvent(TurnstileLane.TWO);
        Thread.sleep(40);
        assertTrue(callable2.isComplete);

        turnstileEventFactory.fireEvent(TurnstileLane.THREE);
        Thread.sleep(40);
        assertTrue(callable3.isComplete);

    }

    @Test
    public void dispenseNotWaitIfInventoryExistsTest() throws InterruptedException {

        TurnstileEventFactory turnstileEventFactory = new TurnstileEventFactory(100);

        SelfRefillingSelectionDispenser<TurnstileEvent, TurnstileLane> dispenser =
                new SelfRefillingSelectionDispenser<TurnstileEvent, TurnstileLane>(turnstileEventFactory);

        DispenseCallable callable1 = new DispenseCallable(TurnstileLane.ONE, dispenser);
        DispenseCallable callable2 = new DispenseCallable(TurnstileLane.TWO, dispenser);
        DispenseCallable callable3 = new DispenseCallable(TurnstileLane.THREE, dispenser);


        executorService.submit(callable3);
        executorService.submit(callable2);
        executorService.submit(callable1);

        turnstileEventFactory.fireEvent(TurnstileLane.ONE);
        turnstileEventFactory.fireEvent(TurnstileLane.TWO);
        Thread.sleep(40);

        assertTrue(callable1.isComplete);
        assertTrue(callable2.isComplete);
        assertFalse(callable3.isComplete);

        turnstileEventFactory.fireEvent(TurnstileLane.THREE);
        Thread.sleep(40);

        assertTrue(callable3.isComplete);
    }


    private class DispenseCallable implements Callable<TurnstileEvent> {

        private boolean isComplete = false;
        private TurnstileLane turnstileLane;
        private SelfRefillingSelectionDispenser<TurnstileEvent, TurnstileLane> dispenser;

        DispenseCallable(TurnstileLane turnstilLane, SelfRefillingSelectionDispenser<TurnstileEvent, TurnstileLane> dispenser) {
            this.turnstileLane = turnstilLane;
            this.dispenser = dispenser;
        }

        public TurnstileEvent call() {
            TurnstileEvent event = dispenser.dispense(turnstileLane);
            isComplete = true;
            return event;
        }

    }

}
