package org.atomnuke.listener;

/**
 *
 * @author zinic
 */
public final class AtomListenerResult implements ListenerResult {

   public static AtomListenerResult halt(String message) {
      return new AtomListenerResult(message, ListenerAction.HALT);
   }

   public static AtomListenerResult ok() {
      return new AtomListenerResult("No action.", ListenerAction.NO_ACTION);
   }

   private final ListenerAction action;
   private final String message;

   private AtomListenerResult(String message, ListenerAction action) {
      this.message = message;
      this.action = action;
   }

    @Override
   public ListenerAction action() {
      return action;
   }

    @Override
   public String message() {
      return message;
   }
}
