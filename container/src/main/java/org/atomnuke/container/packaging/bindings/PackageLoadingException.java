package org.atomnuke.container.packaging.bindings;

/**
 *
 * @author zinic
 */
public class PackageLoadingException extends Exception {

   public PackageLoadingException(Throwable cause) {
      super(cause);
   }

   public PackageLoadingException(String message) {
      super(message);
   }

   public PackageLoadingException(String message, Throwable cause) {
      super(message, cause);
   }
}
