package net.jps.nuke.source.impl;

import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.source.AtomSourceResult;

/**
 *
 * @author zinic
 */
public class AtomSourceResultImpl implements AtomSourceResult {

   private final Feed feed;
   private final Entry entry;

   public AtomSourceResultImpl(Feed feed) {
      this(feed, null);
   }

   public AtomSourceResultImpl(Entry entry) {
      this(null, entry);
   }

   private AtomSourceResultImpl(Feed feed, Entry entry) {
      this.feed = feed;
      this.entry = entry;
   }

   @Override
   public boolean isFeedPage() {
      return feed != null;
   }

   @Override
   public Feed feed() {
      return feed;
   }

   @Override
   public Entry entry() {
      return entry;
   }
}
