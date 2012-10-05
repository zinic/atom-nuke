package org.atomnuke.bindings;

/**
 *
 * @author zinic
 */
public class BindingLoaderException extends Exception {

   public BindingLoaderException(Throwable cause) {
      super(cause);
   }

   public BindingLoaderException(String message) {
      super(message);
   }

   public BindingLoaderException(String message, Throwable cause) {
      super(message, cause);
   }
}
