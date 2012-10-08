package org.atomnuke.container.packaging.bindings;

/**
 *
 * @author zinic
 */
public class PackageLoaderException extends Exception {

   public PackageLoaderException(Throwable cause) {
      super(cause);
   }

   public PackageLoaderException(String message) {
      super(message);
   }

   public PackageLoaderException(String message, Throwable cause) {
      super(message, cause);
   }
}
