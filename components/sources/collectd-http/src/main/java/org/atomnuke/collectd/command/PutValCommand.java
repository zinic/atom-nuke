package org.atomnuke.collectd.command;

/**
 *
 * @author zinic
 */
public class PutValCommand {

   private final String host, plugin, pluginInstance, type, typeInstance, interval, timestamp, value;

   public PutValCommand(String host, String plugin, String pluginInstance, String type, String typeInstance, String interval, String timestamp, String value) {
      this.host = host;
      this.plugin = plugin;
      this.pluginInstance = pluginInstance;
      this.type = type;
      this.typeInstance = typeInstance;
      this.interval = interval;
      this.timestamp = timestamp;
      this.value = value;
   }

   public String host() {
      return host;
   }

   public String plugin() {
      return plugin;
   }

   public String pluginInstance() {
      return pluginInstance;
   }

   public String type() {
      return type;
   }

   public String typeInstance() {
      return typeInstance;
   }

   public String interval() {
      return interval;
   }

   public String timestamp() {
      return timestamp;
   }

   public String value() {
      return value;
   }
}
