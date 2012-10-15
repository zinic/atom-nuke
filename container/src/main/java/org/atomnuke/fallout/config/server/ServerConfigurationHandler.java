package org.atomnuke.fallout.config.server;

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

/**
 *
 * @author zinic
 */
public class ServerConfigurationHandler {

   private final ServerConfiguration configurationCopy;

   public ServerConfigurationHandler(ServerConfiguration configurationCopy) {
      this.configurationCopy = configurationCopy;
   }

   public ServerConfiguration getConfiguration() {
      return configurationCopy;
   }

   public List<Source> getSources() {
      if (configurationCopy.getSources() == null) {
         configurationCopy.setSources(new Sources());
      }

      return configurationCopy.getSources().getSource();
   }

   public List<Sink> getSinks() {
      if (configurationCopy.getSinks() == null) {
         configurationCopy.setSinks(new Sinks());
      }

      return configurationCopy.getSinks().getSink();
   }

   public List<Relay> getRelays() {
      if (configurationCopy.getEps() == null) {
         final EventProcessingSystem eps = new EventProcessingSystem();
         eps.setEventlets(new Eventlets());
         eps.setRelays(new Relays());

         configurationCopy.setEps(eps);
      }

      return configurationCopy.getEps().getRelays().getRelay();
   }

   public List<Eventlet> getEventlets() {
      if (configurationCopy.getEps() == null) {
         final EventProcessingSystem eps = new EventProcessingSystem();
         eps.setEventlets(new Eventlets());
         eps.setRelays(new Relays());

         configurationCopy.setEps(eps);
      }

      return configurationCopy.getEps().getEventlets().getEventlet();
   }

   public List<Binding> getBindings() {
      if (configurationCopy.getBindings() == null) {
         final Bindings bindings = new Bindings();
         configurationCopy.setBindings(bindings);
      }

      return configurationCopy.getBindings().getBinding();
   }

   public Relay findRelay(String id) {
      for (Relay relay : getRelays()) {
         if (relay.getId().equals(id)) {
            return relay;
         }
      }

      return null;
   }

   public Eventlet findEventlet(String eventletId) {
      for (Eventlet eventlet : getEventlets()) {
         if (eventlet.getId().equals(eventletId)) {
            return eventlet;
         }
      }

      return null;
   }

   public Source findSource(String id) {
      for (Source source : getSources()) {
         if (source.getId().equals(id)) {
            return source;
         }
      }

      return null;
   }

   public Sink findSink(String id) {
      for (Sink sink : getSinks()) {
         if (sink.getId().equals(id)) {
            return sink;
         }
      }

      return null;
   }
}
