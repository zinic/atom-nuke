package org.atomnuke.atom.io.cfg;

/**
 * Interface for representing configuration options that may be passed to
 * AtomWriter instances.
 *
 * @author zinic
 */
public interface WriterConfiguration {

   /**
    * Returns the desired XML namespace verbosity of write operations.
    *
    * @return the desired NamespaceLevel.
    */
   NamespaceLevel getNamespaceLevel();
}
