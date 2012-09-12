package org.atomnuke.atom.io;

/**
 * Factory interface for AtomReader instances to simply their creation and, or
 * pooling.
 *
 * @author zinic
 */
public interface AtomReaderFactory {

   /**
    * Gets an AtomReader instance. This may or may not instantiate new objects.
    *
    * @return an AtomReader in a valid state.
    */
   AtomReader getInstance();
}
