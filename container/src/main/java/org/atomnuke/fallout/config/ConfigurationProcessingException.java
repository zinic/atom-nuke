package org.atomnuke.fallout.config;

/**
 *
 * @author zinic
 */
public class ConfigurationProcessingException extends Exception {

   public ConfigurationProcessingException(String message) {
      super(message);
   }

   public ConfigurationProcessingException(String message, Throwable cause) {
      super(message, cause);
   }

   public ConfigurationProcessingException(Throwable cause) {
      super(cause);
   }
}
