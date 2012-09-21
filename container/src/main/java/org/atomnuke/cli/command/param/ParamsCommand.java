package org.atomnuke.cli.command.param;

import java.util.Iterator;
import org.atomnuke.cli.CliConfigurationHandler;
import org.atomnuke.config.model.Eventlet;
import org.atomnuke.config.model.Parameter;
import org.atomnuke.config.model.Parameters;
import org.atomnuke.config.model.Sink;
import org.atomnuke.config.model.Source;
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
         final Parameters params = findParametersFor(arguments[0]);

         if (params != null) {
            final StringBuilder msg = new StringBuilder();

            for (Parameter param : params.getParam()) {
               msg.append("Parameter: name=\"").append(param.getName()).append("\" value=\"").append(param.getValue()).append("\"\n");
            }

            return new CommandSuccess(msg.toString());
         } else {
            return new CommandFailure("No defined configuration element named: " + arguments[0] + ".");
         }
      }

      if (arguments.length == 4) {
         final Parameters params = findParametersFor(arguments[0]);

         if (params == null) {
            throw new IllegalArgumentException("No source, sink or evenlet with name: " + arguments[0]);
         }

         if (arguments[1].equals("rm")) {
            for (Iterator<Parameter> paramItr = params.getParam().iterator(); paramItr.hasNext();) {
               if (paramItr.next().getName().equals(arguments[2])) {
                  paramItr.remove();
               }
            }

            getConfigHandler().write();
            return new CommandSuccess();
         } else if (arguments[1].equals("set")) {
            boolean updated = false;

            for (Parameter param : params.getParam()) {
               if (param.getName().equals(arguments[2])) {
                  param.setValue(arguments[3]);
                  updated = true;
               }
            }

            if (!updated) {
               final Parameter paramToAdd = new Parameter();
               paramToAdd.setName(arguments[2]);
               paramToAdd.setValue(arguments[3]);

               params.getParam().add(paramToAdd);
            }

            getConfigHandler().write();
            
            return new CommandSuccess();
         }
      }

      return new CommandFailure("Usage: <id> [set|rm] <param-name> <param-value>");
   }

   private Parameters findParametersFor(String target) throws ConfigurationException {
      final Eventlet eventlet = getConfigHandler().findEventlet(target);

      if (eventlet != null) {
         Parameters params = eventlet.getParameters();

         if (params == null) {
            params = new Parameters();
            eventlet.setParameters(params);
         }

         return params;
      }

      final Source source = getConfigHandler().findSource(target);

      if (source != null) {
         Parameters params = source.getParameters();

         if (params == null) {
            params = new Parameters();
            source.setParameters(params);
         }

         return params;
      }

//      final Relay relay = getConfigHandler().findRelay(target);
//
//      if (relay != null) {
//         Parameters params = relay.getParameters();
//
//         if (params == null) {
//            params = new Parameters();
//            relay.setParameters(params);
//         }
//
//         return params;
//      }

      final Sink sink = getConfigHandler().findSink(target);

      if (sink != null) {
         Parameters params = sink.getParameters();

         if (params == null) {
            params = new Parameters();
            sink.setParameters(params);
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
}
