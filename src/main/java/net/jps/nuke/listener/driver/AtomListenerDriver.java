package net.jps.nuke.listener.driver;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.listener.RegisteredListener;
import net.jps.nuke.listener.AtomListener;
import net.jps.nuke.listener.ListenerResult;
import net.jps.nuke.listener.ListenerResultImpl;

/**
 *
 * TODO:Refactor - Recompose this into three classes: abstract, feed and entry.
 *
 * @author zinic
 */
public class AtomListenerDriver implements RegisteredListenerDriver {

   private final RegisteredListener registeredListener;
   private final Feed feed;
   private final Entry entry;

   public AtomListenerDriver(RegisteredListener registeredListener, Entry entry) {
      this(registeredListener, null, entry);
   }

   public AtomListenerDriver(RegisteredListener registeredListener, Feed feed) {
      this(registeredListener, feed, null);
   }

   private AtomListenerDriver(RegisteredListener registeredListener, Feed feed, Entry entry) {
      this.registeredListener = registeredListener;
      this.feed = feed;
      this.entry = entry;
   }

   public void run() {
      final ListenerResult result = drive(registeredListener.listener());

      switch (result.getAction()) {
         case FOLLOW_LINK:
            // TODO:Implement
            break;

         case HALT:
            registeredListener.cancellationRemote().cancel();
            break;

         default:
      }
   }

   private ListenerResult drive(AtomListener listener) {
      try {
         if (feed != null) {
            return listener.feedPage(feed);
         } else if (entry != null) {
            return listener.entry(entry);
         }
      } catch (Exception ex) {
         // TODO:Log
         ex.printStackTrace(System.err);

         return ListenerResultImpl.halt(ex.getMessage());
      }

      return ListenerResultImpl.halt("Feed document was null.");
   }
}
