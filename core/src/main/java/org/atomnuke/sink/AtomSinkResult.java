package org.atomnuke.sink;

/**
 *
 * @author zinic
 */
public final class AtomSinkResult implements SinkResult {

   public static AtomSinkResult halt(String message) {
      return new AtomSinkResult(message, SinkAction.HALT);
   }

   public static AtomSinkResult ok() {
      return new AtomSinkResult("No action, everything is okay.", SinkAction.OK);
   }

   private final SinkAction action;
   private final String message;

   private AtomSinkResult(String message, SinkAction action) {
      this.message = message;
      this.action = action;
   }

    @Override
   public SinkAction action() {
      return action;
   }

    @Override
   public String message() {
      return message;
   }
}
