package net.jps.nuke.cli.command;

import net.jps.nuke.config.ConfigurationReader;
import net.jps.nuke.util.cli.command.AbstractCommand;
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
}
