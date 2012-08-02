package net.jps.nuke.listener;

import net.jps.nuke.atom.model.Link;

/**
 *
 * @author zinic
 */
public final class AtomListenerResult implements ListenerResult {

   public static AtomListenerResult halt(String message) {
      return new AtomListenerResult(message, ListenerAction.HALT, null);
   }

   public static AtomListenerResult ok() {
      return new AtomListenerResult("No action.", ListenerAction.NO_ACTION, null);
   }
   
   private final ListenerAction action;
   private final String message;
   private final Link followLink;

   private AtomListenerResult(String message, ListenerAction action, Link linkToFollow) {
      this.message = message;
      this.action = action;
      this.followLink = linkToFollow;
   }

    @Override
   public ListenerAction getAction() {
      return action;
   }

    @Override
   public Link getLink() {
      return followLink;
   }

    @Override
   public String getMessage() {
      return message;
   }
}
