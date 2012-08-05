package org.atomnuke.cli.command;

import java.util.List;
import org.atomnuke.config.ConfigurationException;
import org.atomnuke.config.ConfigurationHandler;
import org.atomnuke.config.ConfigurationReader;
import org.atomnuke.config.model.Binding;
import org.atomnuke.config.model.Bindings;
import org.atomnuke.config.model.EventProcessingSystem;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.config.model.Eventlets;
import org.atomnuke.config.model.Relay;
import org.atomnuke.config.model.Relays;
import org.atomnuke.config.model.Sink;
import org.atomnuke.config.model.Sinks;
import org.atomnuke.config.model.Source;
import org.atomnuke.config.model.Sources;
import org.atomnuke.util.cli.command.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public abstract class AbstractNukeCommand extends AbstractCommand {

   private final ConfigurationReader configurationReader;
   private final Logger logger;

   public AbstractNukeCommand(ConfigurationReader configurationReader) {
      this.configurationReader = configurationReader;
      this.logger = logger();
   }

   private Logger logger() {
      return LoggerFactory.getLogger(getClass());
   }

   protected Logger log() {
      return logger;
   }

   protected final ConfigurationReader getConfigurationReader() {
      return configurationReader;
   }

   protected List<Source> getSources(ConfigurationHandler cfgHandler) throws ConfigurationException {
      if (cfgHandler.getConfiguration().getSources() == null) {
         cfgHandler.getConfiguration().setSources(new Sources());
         cfgHandler.write();
      }

      return cfgHandler.getConfiguration().getSources().getSource();
   }

   protected List<Sink> getSinks(ConfigurationHandler cfgHandler) throws ConfigurationException {
      if (cfgHandler.getConfiguration().getSinks() == null) {
         cfgHandler.getConfiguration().setSinks(new Sinks());
         cfgHandler.write();
      }

      return cfgHandler.getConfiguration().getSinks().getSink();
   }

   protected List<Relay> getRelays(ConfigurationHandler cfgHandler) throws ConfigurationException {
      if (cfgHandler.getConfiguration().getEps() == null) {
         final EventProcessingSystem eps = new EventProcessingSystem();
         eps.setEventlets(new Eventlets());
         eps.setRelays(new Relays());

         cfgHandler.getConfiguration().setEps(eps);
         cfgHandler.write();
      }

      return cfgHandler.getConfiguration().getEps().getRelays().getRelay();
   }

   protected List<Eventlet> getEventlets(ConfigurationHandler cfgHandler) throws ConfigurationException {
      if (cfgHandler.getConfiguration().getEps() == null) {
         final EventProcessingSystem eps = new EventProcessingSystem();
         eps.setEventlets(new Eventlets());
         eps.setRelays(new Relays());

         cfgHandler.getConfiguration().setEps(eps);
         cfgHandler.write();
      }

      return cfgHandler.getConfiguration().getEps().getEventlets().getEventlet();
   }

   protected List<Binding> getBindings(ConfigurationHandler cfgHandler) throws ConfigurationException {
      if (cfgHandler.getConfiguration().getBindings() == null) {
         final Bindings bindings = new Bindings();
         cfgHandler.getConfiguration().setBindings(bindings);
      }

      return cfgHandler.getConfiguration().getBindings().getBinding();
   }

   protected Relay findRelay(String id) throws ConfigurationException {
      for (Relay relay : getRelays(getConfigurationReader().readConfiguration())) {
         if (relay.getId().equals(id)) {
            return relay;
         }
      }

      return null;
   }

   protected Eventlet findEventlet(String eventletId) throws ConfigurationException {
      for (Eventlet eventlet : getEventlets(getConfigurationReader().readConfiguration())) {
         if (eventlet.getId().equals(eventletId)) {
            return eventlet;
         }
      }

      return null;
   }

   protected Source findSource(String id) throws ConfigurationException {
      for (Source source : getSources(getConfigurationReader().readConfiguration())) {
         if (source.getId().equals(id)) {
            return source;
         }
      }

      return null;
   }

   protected Sink findSink(String id) throws ConfigurationException {
      for (Sink sink : getSinks(getConfigurationReader().readConfiguration())) {
         if (sink.getId().equals(id)) {
            return sink;
         }
      }

      return null;
   }
}
