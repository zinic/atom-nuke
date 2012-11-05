package org.atomnuke.pubsub.api.type;

/**
 *
 * @author zinic
 */
public class SubscriptionContent {

   private String timestamp, value;

   public String getTimestamp() {
      return timestamp;
   }

   public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }
}
