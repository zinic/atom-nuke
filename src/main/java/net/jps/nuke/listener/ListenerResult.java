package net.jps.nuke.listener;

import net.jps.nuke.atom.model.Link;

/**
 *
 * @author zinic
 */
public final class ListenerResult {

   public static ListenerResult follow(Link linkToFollow) {
      return new ListenerResult("Follow link.", ListenerAction.FOLLOW_LINK, linkToFollow);
   }

   public static ListenerResult halt(String message) {
      return new ListenerResult(message, ListenerAction.HALT, null);
   }

   public static ListenerResult noAction() {
      return new ListenerResult("No action.", ListenerAction.NO_ACTION, null);
   }
   
   private final ListenerAction action;
   private final String message;
   private final Link followLink;

   private ListenerResult(String message, ListenerAction action, Link linkToFollow) {
      this.message = message;
      this.action = action;
      this.followLink = linkToFollow;
   }

   public ListenerAction getAction() {
      return action;
   }

   public Link getLink() {
      return followLink;
   }

   public String getMessage() {
      return message;
   }
}
