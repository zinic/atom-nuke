package org.atomnuke.fallout.context.config;

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
