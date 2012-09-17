package org.atomnuke.container.service.construct;

/**
 *
 * @author zinic
 */
public class ConstructionException extends Exception {

   public ConstructionException(String message) {
      super(message);
   }

   public ConstructionException(String message, Throwable cause) {
      super(message, cause);
   }

   public ConstructionException(Throwable cause) {
      super(cause);
   }
}
