package org.atomnuke.cli.command;

import java.util.Iterator;
import org.atomnuke.atombus.config.model.Binding;
import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public abstract class AbstractNukeCommand extends AbstractCommand {

   private final CliConfigurationHandler configurationHandler;
   private final Logger logger;

   public AbstractNukeCommand(CliConfigurationHandler configurationHandler) {
      this.configurationHandler = configurationHandler;
      this.logger = logger();
   }

   private Logger logger() {
      return LoggerFactory.getLogger(getClass());
   }

   protected Logger log() {
      return logger;
   }

   protected final CliConfigurationHandler getConfigHandler() {
      return configurationHandler;
   }

   protected void unbindSink(CliConfigurationHandler cfgHandler, String sinkActorId) throws ConfigurationException {
      for (Iterator<Binding> bindingItr = cfgHandler.getBindings().iterator(); bindingItr.hasNext();) {
         if (bindingItr.next().getSinkActor().equals(sinkActorId)) {
            bindingItr.remove();
         }
      }

      cfgHandler.write();
   }

   protected void unbindSource(CliConfigurationHandler cfgHandler, String sourceActorId) throws ConfigurationException {
      for (Iterator<Binding> bindingItr = cfgHandler.getBindings().iterator(); bindingItr.hasNext();) {
         if (bindingItr.next().getSourceActor().equals(sourceActorId)) {
            bindingItr.remove();
         }
      }

      cfgHandler.write();
   }
}
