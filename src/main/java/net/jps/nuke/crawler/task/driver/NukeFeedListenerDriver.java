package net.jps.nuke.crawler.task.driver;

import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.crawler.task.RegisteredListener;
import net.jps.nuke.listener.FeedListener;
import net.jps.nuke.listener.ListenerResult;

/**
 *
 * @author zinic
 */
public class NukeFeedListenerDriver implements RegisteredListenerDriver {

   private final RegisteredListener registeredListener;
   private final Feed feed;

   public NukeFeedListenerDriver(RegisteredListener registeredListener, Feed feed) {
      this.registeredListener = registeredListener;
      this.feed = feed;
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

   private ListenerResult drive(FeedListener listener) {
      try {
         return feed != null ? listener.readPage(feed) : ListenerResult.halt("Feed document was null.");
      } catch (Exception ex) {
         // TODO:Log
         ex.printStackTrace(System.err);

         return ListenerResult.halt(ex.getMessage());
      }
   }
}
