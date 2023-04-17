package io.github.ericdriggs.selectiondispenser.dispenser.turnstyle;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by edriggs on 10/11/15.
 */
public class TurnstileEventTest {

    @BeforeEach
    void logTestName(TestInfo testInfo) {
        String methodName = testInfo.getTestMethod().orElseThrow().getName();
        System.out.println("Test " + getClass().getSimpleName() + "::" + methodName);
    }

    @Test
    public void waitTimeouExceededTest() {
        TurnstileController turnstyleController = new TurnstileController();
        Assertions.assertThrows(TimeoutException.class, () -> {
            turnstyleController.waitForEvent(TurnstileLane.ONE, 0L);
        });
    }

    @Test
    public void fireEventBeforeWaitTest() throws TimeoutException {
        TurnstileController turnstyleController = new TurnstileController();
        final TurnstileLane lane = TurnstileLane.ONE;
        turnstyleController.fireLaneEvent(lane);
        TurnstileEvent event = turnstyleController.waitForEvent(lane, 0L);
        assertNotNull(event);
        assertEquals(event.getLane(), lane);
    }

    @Test
    public void fireEventBeforeTimeoutTest() throws InterruptedException {
        TurnstileController turnstyleController = new TurnstileController();
        final TurnstileLane lane = TurnstileLane.ONE;

        WaitForTurnstyleEventCallable waitForCallable =
                new WaitForTurnstyleEventCallable(lane, turnstyleController, 100);
        FireTurnstyleEventRunnable fireEventRunnable = new FireTurnstyleEventRunnable(lane, turnstyleController);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(waitForCallable);

        //sleep for less than timeout
        Thread.sleep(20);

        executor.submit(fireEventRunnable);
        executor.shutdown();
        executor.awaitTermination(2000, TimeUnit.MILLISECONDS);
        assertTrue(waitForCallable.isSuccess());
        assertTrue(fireEventRunnable.isSuccess());
    }

    @Test
    public void fireEventAfterTimeoutTest() throws InterruptedException {
        TurnstileController turnstyleController = new TurnstileController();
        final TurnstileLane lane = TurnstileLane.ONE;

        WaitForTurnstyleEventCallable waitForCallable = new WaitForTurnstyleEventCallable(lane, turnstyleController, 100);
        FireTurnstyleEventRunnable fireEventRunnable = new FireTurnstyleEventRunnable(lane, turnstyleController);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(waitForCallable);

        //sleep for more than timeout
        Thread.sleep(200);

        executor.submit(fireEventRunnable);
        executor.shutdown();
        executor.awaitTermination(2000, TimeUnit.MILLISECONDS);

        assertFalse(waitForCallable.isSuccess());
        assertTrue(fireEventRunnable.isSuccess());
    }
}
