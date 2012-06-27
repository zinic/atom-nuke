package net.jps.nuke.abdera.listener;

import org.apache.abdera.model.Link;

/**
 *
 * @author zinic
 */
public class ListenerResult {

   public static ListenerResult follow(Link linkToFollow) {
      return new ListenerResult("Follow link.", ListenerAction.FOLLOW_LINK, linkToFollow);
   }

   public static ListenerResult halt(String message) {
      return new ListenerResult(message, ListenerAction.HALT, null);
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
