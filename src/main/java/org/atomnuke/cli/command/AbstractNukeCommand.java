package org.atomnuke.cli.command;

import java.util.Iterator;
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

   protected void unbindReciever(ConfigurationHandler cfgHandler, String receiverId) throws ConfigurationException {
      for (Iterator<Binding> bindingItr = cfgHandler.getBindings().iterator(); bindingItr.hasNext();) {
         if (bindingItr.next().getReceiver().equals(receiverId)) {
            bindingItr.remove();
         }
      }

      cfgHandler.write();
   }

   protected void unbindTarget(ConfigurationHandler cfgHandler, String targetId) throws ConfigurationException {
      for (Iterator<Binding> bindingItr = cfgHandler.getBindings().iterator(); bindingItr.hasNext();) {
         if (bindingItr.next().getTarget().equals(targetId)) {
            bindingItr.remove();
         }
      }

      cfgHandler.write();
   }
}
