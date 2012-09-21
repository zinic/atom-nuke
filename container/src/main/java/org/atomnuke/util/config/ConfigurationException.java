package org.atomnuke.util.config;

/**
 *
 * @author zinic
 */
public class ConfigurationException extends Exception {

   public ConfigurationException(Throwable cause) {
      super(cause);
   }

   public ConfigurationException(String message, Throwable cause) {
      super(message, cause);
   }

   public ConfigurationException(String message) {
      super(message);
   }
}
