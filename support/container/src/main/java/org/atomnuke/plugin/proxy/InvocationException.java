package org.atomnuke.plugin.proxy;

/**
 *
 * @author zinic
 */
public class InvocationException extends RuntimeException {

   public InvocationException(String message) {
      super(message);
   }

   public InvocationException(String message, Throwable cause) {
      super(message, cause);
   }

   public InvocationException(Throwable cause) {
      super(cause);
   }
}
