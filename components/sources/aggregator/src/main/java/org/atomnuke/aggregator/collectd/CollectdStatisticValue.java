package org.atomnuke.aggregator.collectd;

/**
 *
 * @author zinic
 */
public class CollectdStatisticValue {

   private String timestamp;
   private String value;

   public String getTimestamp() {
      return timestamp;
   }

   public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
   }

   public double getValueAsDouble() {
      return Double.parseDouble(getValue());
   }

   public String getValue() {
      return value;
   }

   public void setValue(String value) {
      this.value = value;
   }
}
