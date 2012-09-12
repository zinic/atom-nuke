package org.atomnuke.atom.io;

/**
 * Factory interface for AtomWriter instances to simply their creation and, or
 * pooling.
 *
 * @author zinic
 */
public interface AtomWriterFactory {

   /**
    * Gets an AtomWriter instance. This may or may not instantiate new objects.
    *
    * @return an AtomWriter in a valid state.
    */
   AtomWriter getInstance();
}
