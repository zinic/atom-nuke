package org.atomnuke.cli.command.param;

import java.util.Iterator;
import org.atomnuke.atombus.config.model.MessageActor;
import org.atomnuke.atombus.config.model.Parameter;
import org.atomnuke.atombus.config.model.Parameters;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.util.cli.command.AbstractCommandList;
import org.atomnuke.util.cli.command.result.CommandFailure;
import org.atomnuke.util.cli.command.result.CommandResult;
import org.atomnuke.util.cli.command.result.CommandSuccess;
import org.atomnuke.util.config.ConfigurationException;

/**
 *
 * @author zinic
 */
public class ParamsCommand extends AbstractCommandList {

   public ParamsCommand(CliConfigurationHandler cfgHandler) {
      super(cfgHandler);
   }

   @Override
   public CommandResult perform(String[] arguments) throws Exception {
      if (arguments.length == 1) {
         return listParameters(arguments[0]);
      } else if (arguments.length == 3 && "rm".equals(arguments[1])) {
         return removeParameter(arguments[0], arguments[2]);
      } else if (arguments.length == 4 && "set".equals(arguments[1])) {
         return setParameter(arguments[0], arguments[2], arguments[3]);
      }

      return new CommandFailure("Usage: <id> [set|rm] <param-name> <param-value>");
   }

   private Parameters findParametersFor(String actorId) throws ConfigurationException {
      final MessageActor messageActor = getConfigHandler().findMessageActor(actorId);

      if (messageActor != null) {
         Parameters params = messageActor.getParameters();

         if (params == null) {
            params = new Parameters();
            messageActor.setParameters(params);
         }

         return params;
      }

      return null;
   }

   @Override
   public String getCommandToken() {
      return "params";
   }

   @Override
   public String getCommandDescription() {
      return "Sets configuration parameters for sources, sinks and eventlets.";
   }

   private CommandResult removeParameter(String actorId, String parameterId) throws ConfigurationException, IllegalArgumentException {
      final Parameters params = findParametersFor(actorId);

      if (params == null) {
         throw new IllegalArgumentException("No message actor defined with name: " + actorId);
      }

      for (Iterator<Parameter> paramItr = params.getParam().iterator(); paramItr.hasNext();) {
         if (paramItr.next().getName().equals(parameterId)) {
            paramItr.remove();
         }
      }

      getConfigHandler().write();
      return new CommandSuccess();
   }

   private CommandResult setParameter(String actorId, String parmeterId, String parameterValue) throws ConfigurationException {
      final Parameters params = findParametersFor(actorId);

      boolean updated = false;

      for (Parameter param : params.getParam()) {
         if (param.getName().equals(parmeterId)) {
            param.setValue(parameterValue);
            updated = true;
         }
      }

      if (!updated) {
         final Parameter paramToAdd = new Parameter();
         paramToAdd.setName(parmeterId);
         paramToAdd.setValue(parameterValue);

         params.getParam().add(paramToAdd);
      }

      getConfigHandler().write();
      return new CommandSuccess();
   }

   private CommandResult listParameters(String actorId) throws ConfigurationException, IllegalArgumentException {
      final Parameters params = findParametersFor(actorId);

      if (params != null) {
         final StringBuilder msg = new StringBuilder();

         for (Parameter param : params.getParam()) {
            msg.append("Parameter: name=\"").append(param.getName()).append("\" value=\"").append(param.getValue()).append("\"\n");
         }

         return new CommandSuccess(msg.toString());
      } else {
         throw new IllegalArgumentException("No message actor defined with name: " + actorId);
      }
   }
}
