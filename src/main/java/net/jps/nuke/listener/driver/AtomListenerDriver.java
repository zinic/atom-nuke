package net.jps.nuke.listener.driver;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.listener.AtomListener;
import net.jps.nuke.listener.AtomListenerResult;
import net.jps.nuke.listener.ListenerResult;
import net.jps.nuke.listener.RegisteredListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class AtomListenerDriver implements RegisteredListenerDriver {

   private static final Logger LOG = LoggerFactory.getLogger(AtomListenerDriver.class);
   
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
  
   @Override
   public void run() {
      final ListenerResult result = drive(registeredListener.listener());

      switch (result.getAction()) {
         case HALT:
            registeredListener.cancel();
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
         LOG.error(ex.getMessage(), ex);

         return AtomListenerResult.halt(ex.getMessage());
      }

      return AtomListenerResult.halt("Feed document was null.");
   }
}
