/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atomnuke.cli.command.actor;

import org.atomnuke.atombus.config.model.LanguageType;
import org.atomnuke.atombus.config.model.MessageActor;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.cli.command.AbstractNukeCommand;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;

/**
 *
 * @author zinic
 */
public class AddActor extends AbstractNukeCommand {

   private static final int ACTOR_ID = 0, SOURCE_LANG = 1, SOURCE_REFERENCE = 2;

   public AddActor(CliConfigurationHandler configurationHandler) {
      super(configurationHandler);
   }

   @Override
   public String getCommandToken() {
      return "add";
   }

   @Override
   public String getCommandDescription() {
      return "Adds a new message actor definition.";
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length < 1 || arguments.length > 3) {
         return new CommandFailure("Usage: <actor-id> <language-type> <code-ref>");
      }

      final CliConfigurationHandler cfgHandler = getConfigHandler();

      if (cfgHandler.findMessageActor(arguments[ACTOR_ID]) != null) {
         return new CommandFailure("A message actor with the id \"" + arguments[ACTOR_ID] + "\" already exists.");
      }

      final LanguageType sinkLanguageType;

      try {
         sinkLanguageType = LanguageType.fromValue(arguments[SOURCE_LANG].toLowerCase());
      } catch (IllegalArgumentException iae) {
         return new CommandFailure("Language must be one of: java, javascript, python.");
      }

      final MessageActor newActor = new MessageActor();
      newActor.setId(arguments[ACTOR_ID]);
      newActor.setType(sinkLanguageType);
      newActor.setHref(arguments[SOURCE_REFERENCE]);

      cfgHandler.getMessageActors().add(newActor);
      cfgHandler.write();

      return new CommandSuccess();
   }
}
