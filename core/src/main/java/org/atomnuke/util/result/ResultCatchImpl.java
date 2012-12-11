package org.atomnuke.util.result;

/**
 *
 * @author zinic
 */
public class ResultCatchImpl<T> implements ResultCatch<T> {

   private T result;

   @Override
   public boolean hasResult() {
      return result != null;
   }

   @Override
   public T result() {
      return result;
   }

   @Override
   public void setResult(T result) {
      this.result = result;
   }
}
