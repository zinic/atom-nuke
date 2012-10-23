package org.atomnuke.util;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author zinic
 */
public class WeakReferenceCollection<R> implements ReferenceCollection<R> {

   private class WeakReferenceIterator implements Iterator<R> {

      private final Iterator<WeakReference<R>> delegateItr;

      public WeakReferenceIterator(Iterator<WeakReference<R>> delegateItr) {
         this.delegateItr = delegateItr;
      }

      @Override
      public boolean hasNext() {
         return delegateItr.hasNext();
      }

      @Override
      public R next() {
         do {
            final WeakReference<R> next = delegateItr.next();
            final R nextItem = next.get();

            if (nextItem != null) {
               return nextItem;
            }

            remove();
         } while (hasNext());

         return null;
      }

      @Override
      public void remove() {
         delegateItr.remove();
      }
   }
   private final List<WeakReference<R>> referenceList;

   public WeakReferenceCollection(List<WeakReference<R>> referenceList) {
      this.referenceList = referenceList;
   }

   @Override
   public Iterator<R> iterator() {
      return new WeakReferenceIterator(referenceList.iterator());
   }

   @Override
   public void add(R reference) {
      referenceList.add(new WeakReference<R>(reference));
   }
}
