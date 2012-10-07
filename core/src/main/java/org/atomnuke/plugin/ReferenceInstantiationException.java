package org.atomnuke.plugin;

/**
 *
 * @author zinic
 */
public class ReferenceInstantiationException extends Exception {

   public ReferenceInstantiationException(String message) {
      super(message);
   }

   public ReferenceInstantiationException(String message, Throwable cause) {
      super(message, cause);
   }

   public ReferenceInstantiationException(Throwable cause) {
      super(cause);
   }
}
