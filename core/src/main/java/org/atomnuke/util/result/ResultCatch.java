package org.atomnuke.util.result;

/**
 *
 * @author zinic
 */
public interface ResultCatch<T> {

   boolean hasResult();

   T result();

   void setResult(T result);
}
