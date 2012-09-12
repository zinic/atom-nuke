package org.atomnuke.config.server;

import org.atomnuke.util.config.ConfigurationException;
import java.util.List;
import org.atomnuke.config.model.Binding;
import org.atomnuke.config.model.Bindings;
import org.atomnuke.config.model.EventProcessingSystem;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.config.model.Eventlets;
import org.atomnuke.config.model.Relay;
import org.atomnuke.config.model.Relays;
import org.atomnuke.config.model.ServerConfiguration;
import org.atomnuke.config.model.Sink;
import org.atomnuke.config.model.Sinks;
import org.atomnuke.config.model.Source;
import org.atomnuke.config.model.Sources;
import org.atomnuke.util.config.io.ConfigurationManager;

/**
 *
 * @author zinic
 */
public class ServerConfigurationHandler {

   private final ConfigurationManager<ServerConfiguration> configurationManager;
   private final ServerConfiguration configurationCopy;

   public ServerConfigurationHandler(ConfigurationManager<ServerConfiguration> configurationManager, ServerConfiguration configurationCopy) {
      this.configurationManager = configurationManager;
      this.configurationCopy = configurationCopy;
   }

   public void write() throws ConfigurationException {
      configurationManager.write(configurationCopy);
   }

   public ServerConfiguration getConfiguration() {
      return configurationCopy;
   }

   public List<Source> getSources() throws ConfigurationException {
      if (configurationCopy.getSources() == null) {
         configurationCopy.setSources(new Sources());
      }

      return configurationCopy.getSources().getSource();
   }

   public List<Sink> getSinks() throws ConfigurationException {
      if (configurationCopy.getSinks() == null) {
         configurationCopy.setSinks(new Sinks());
      }

      return configurationCopy.getSinks().getSink();
   }

   public List<Relay> getRelays() throws ConfigurationException {
      if (configurationCopy.getEps() == null) {
         final EventProcessingSystem eps = new EventProcessingSystem();
         eps.setEventlets(new Eventlets());
         eps.setRelays(new Relays());

         configurationCopy.setEps(eps);
      }

      return configurationCopy.getEps().getRelays().getRelay();
   }

   public List<Eventlet> getEventlets() throws ConfigurationException {
      if (configurationCopy.getEps() == null) {
         final EventProcessingSystem eps = new EventProcessingSystem();
         eps.setEventlets(new Eventlets());
         eps.setRelays(new Relays());

         configurationCopy.setEps(eps);
      }

      return configurationCopy.getEps().getEventlets().getEventlet();
   }

   public List<Binding> getBindings() throws ConfigurationException {
      if (configurationCopy.getBindings() == null) {
         final Bindings bindings = new Bindings();
         configurationCopy.setBindings(bindings);
      }

      return configurationCopy.getBindings().getBinding();
   }

   public Relay findRelay(String id) throws ConfigurationException {
      for (Relay relay : getRelays()) {
         if (relay.getId().equals(id)) {
            return relay;
         }
      }

      return null;
   }

   public Eventlet findEventlet(String eventletId) throws ConfigurationException {
      for (Eventlet eventlet : getEventlets()) {
         if (eventlet.getId().equals(eventletId)) {
            return eventlet;
         }
      }

      return null;
   }

   public Source findSource(String id) throws ConfigurationException {
      for (Source source : getSources()) {
         if (source.getId().equals(id)) {
            return source;
         }
      }

      return null;
   }

   public Sink findSink(String id) throws ConfigurationException {
      for (Sink sink : getSinks()) {
         if (sink.getId().equals(id)) {
            return sink;
         }
      }

      return null;
   }
}
