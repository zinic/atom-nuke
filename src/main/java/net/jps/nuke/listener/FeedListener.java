package net.jps.nuke.listener;

import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public interface FeedListener {

   void init();

   void destroy();

   ListenerResult readPage(Feed page);
   
   TimeValue listenerInterval();
}
