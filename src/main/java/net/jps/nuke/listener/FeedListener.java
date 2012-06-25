package net.jps.nuke.listener;

import org.apache.abdera.model.Feed;

/**
 *
 * @author zinic
 */
public interface FeedListener {

   void init();

   void destroy();

   ListenerResult readPage(Feed page);
}
