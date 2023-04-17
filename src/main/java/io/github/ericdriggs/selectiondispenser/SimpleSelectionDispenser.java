package io.github.ericdriggs.selectiondispenser;

/**
 * A thread-safe blocking dispenser
 *
 * @see SelectionDispenser
 */
public class SimpleSelectionDispenser<T, E> extends AbstractSelectionDispenser<T, E> {

    public T dispense(E selection) {
        initSelectionInventory(selection);
        return inventory.get(selection).poll();
    }

}
