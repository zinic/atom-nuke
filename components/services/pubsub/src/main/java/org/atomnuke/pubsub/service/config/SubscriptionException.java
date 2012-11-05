package org.atomnuke.pubsub.service.config;

/**
 *
 * @author zinic
 */
public class SubscriptionException extends Exception {

   public SubscriptionException(String message) {
      super(message);
   }

   public SubscriptionException(String message, Throwable cause) {
      super(message, cause);
   }

   public SubscriptionException(Throwable cause) {
      super(cause);
   }
}
