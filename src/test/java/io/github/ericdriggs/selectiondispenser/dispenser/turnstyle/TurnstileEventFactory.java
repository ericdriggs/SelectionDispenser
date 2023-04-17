package io.github.ericdriggs.selectiondispenser.dispenser.turnstyle;

import io.github.ericdriggs.selectiondispenser.SelectionFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by edriggs on 10/18/15.
 */
public class TurnstileEventFactory implements SelectionFactory<TurnstileEvent, TurnstileLane > {

    ExecutorService executorService = Executors.newCachedThreadPool();
    private final TurnstileController turnstileController = new TurnstileController();
    private final int timeout;

    public TurnstileEventFactory(int timeout) {
        this.timeout = timeout;
    }

    public TurnstileController getTurnstileController() {
        return turnstileController;
    }

    /**
     *
     * @param selection
     * @return a TurnstileEvent after an event has been fired for the selected lane, or <code>NULL</code> if timeout
     */
    public TurnstileEvent createItem(TurnstileLane selection) {

        TurnstileEvent turnstileEvent = null;
        try {
            turnstileEvent = new WaitForTurnstyleEventCallable(selection, turnstileController, timeout).call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return turnstileEvent;
    }

    public void fireEvent( TurnstileLane turnstileLane) {
        new FireTurnstyleEventRunnable(turnstileLane, turnstileController).run();
    }
}
