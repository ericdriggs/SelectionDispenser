package io.github.ericdriggs.selectiondispenser.dispenser.crayon;

import io.github.ericdriggs.selectiondispenser.SelectionFactory;

/**
 * Example factory for refilling selection dispenser
 */
public class CrayonFactory implements SelectionFactory<Crayon, CrayonColor> {

    public Crayon createItem(CrayonColor selection) {
        return new Crayon(selection);
    }
}
