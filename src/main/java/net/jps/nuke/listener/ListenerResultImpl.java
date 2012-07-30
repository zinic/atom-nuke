package net.jps.nuke.listener;

import net.jps.nuke.atom.model.Link;

/**
 *
 * @author zinic
 */
public final class ListenerResultImpl implements ListenerResult {

   public static ListenerResultImpl follow(Link linkToFollow) {
      return new ListenerResultImpl("Follow link.", ListenerAction.FOLLOW_LINK, linkToFollow);
   }

   public static ListenerResultImpl halt(String message) {
      return new ListenerResultImpl(message, ListenerAction.HALT, null);
   }

   public static ListenerResultImpl ok() {
      return new ListenerResultImpl("No action.", ListenerAction.NO_ACTION, null);
   }
   
   private final ListenerAction action;
   private final String message;
   private final Link followLink;

   private ListenerResultImpl(String message, ListenerAction action, Link linkToFollow) {
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
