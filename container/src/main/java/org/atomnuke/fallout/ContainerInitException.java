package org.atomnuke.fallout;

/**
 *
 * @author zinic
 */
public class ContainerInitException extends RuntimeException {

   public ContainerInitException(String message) {
      super(message);
   }

   public ContainerInitException(String message, Throwable cause) {
      super(message, cause);
   }

   public ContainerInitException(Throwable cause) {
      super(cause);
   }
}
