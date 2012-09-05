package org.atomnuke.config;

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
public class ConfigurationHandler {

   private final ConfigurationManager managerReference;
   private final ServerConfiguration configuration;

   public ConfigurationHandler(ConfigurationManager managerReference, ServerConfiguration configuration) {
      this.managerReference = managerReference;
      this.configuration = configuration;
   }

   public void write() throws ConfigurationException {
      managerReference.write(configuration);
   }

   public ServerConfiguration getConfiguration() {
      return configuration;
   }
   
   public List<Source> getSources() throws ConfigurationException {
      if (configuration.getSources() == null) {
         configuration.setSources(new Sources());
         write();
      }

      return configuration.getSources().getSource();
   }

   public List<Sink> getSinks() throws ConfigurationException {
      if (configuration.getSinks() == null) {
         configuration.setSinks(new Sinks());
         write();
      }

      return configuration.getSinks().getSink();
   }

   public List<Relay> getRelays() throws ConfigurationException {
      if (configuration.getEps() == null) {
         final EventProcessingSystem eps = new EventProcessingSystem();
         eps.setEventlets(new Eventlets());
         eps.setRelays(new Relays());

         configuration.setEps(eps);
         write();
      }

      return configuration.getEps().getRelays().getRelay();
   }

   public List<Eventlet> getEventlets() throws ConfigurationException {
      if (configuration.getEps() == null) {
         final EventProcessingSystem eps = new EventProcessingSystem();
         eps.setEventlets(new Eventlets());
         eps.setRelays(new Relays());

         configuration.setEps(eps);
         write();
      }

      return configuration.getEps().getEventlets().getEventlet();
   }

   public List<Binding> getBindings() throws ConfigurationException {
      if (configuration.getBindings() == null) {
         final Bindings bindings = new Bindings();
         configuration.setBindings(bindings);
      }

      return configuration.getBindings().getBinding();
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
