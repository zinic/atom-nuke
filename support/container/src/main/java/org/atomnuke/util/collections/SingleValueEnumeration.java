package org.atomnuke.util.collections;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class SingleValueEnumeration<E> implements Enumeration<E> {

   private final E value;
   private boolean hasMoreElements;

   public SingleValueEnumeration(E value) {
      this.value = value;
      hasMoreElements = true;
   }

   @Override
   public boolean hasMoreElements() {
      return hasMoreElements;
   }

   @Override
   public E nextElement() {
      if (!hasMoreElements) {
         throw new NoSuchElementException();
      }

      // Set this so we can be done with this enumeration
      hasMoreElements = false;

      return value;
   }
}
