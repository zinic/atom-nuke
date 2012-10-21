package org.atomnuke.util;

/**
 *
 * @author zinic
 */
public interface ReferenceCollection<R> extends Iterable<R> {

   void add(R reference);
}
