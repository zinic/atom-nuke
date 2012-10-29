package org.atomnuke.util.collections;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class EmptyEnumeration implements Enumeration<Object> {

   private static final Enumeration INSTANCE = new EmptyEnumeration();

   public static Enumeration<?> instance() {
      return INSTANCE;
   }

   private EmptyEnumeration() {
   }

   @Override
   public boolean hasMoreElements() {
      return false;
   }

   @Override
   public Object nextElement() {
      throw new NoSuchElementException();
   }
}
