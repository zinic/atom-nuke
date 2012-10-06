package org.atomnuke.bindings;

/**
 *
 * @author zinic
 */
public class BindingInstantiationException extends Exception {

   public BindingInstantiationException(Throwable cause) {
      super(cause);
   }

   public BindingInstantiationException(String message) {
      super(message);
   }

   public BindingInstantiationException(String message, Throwable cause) {
      super(message, cause);
   }
}
