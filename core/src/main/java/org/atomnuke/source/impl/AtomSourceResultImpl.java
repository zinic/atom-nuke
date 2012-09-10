package org.atomnuke.source.impl;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.source.AtomSourceResult;

/**
 *
 * @author zinic
 */
public class AtomSourceResultImpl implements AtomSourceResult {

   private final Feed feed;
   private final Entry entry;

   public AtomSourceResultImpl() {
      this(null, null);
   }

   public AtomSourceResultImpl(Feed feed) {
      this(feed, null);
   }

   public AtomSourceResultImpl(Entry entry) {
      this(null, entry);
   }

   public AtomSourceResultImpl(Feed feed, Entry entry) {
      this.feed = feed;
      this.entry = entry;
   }

   @Override
   public boolean isEmpty() {
      return feed == null && entry == null;
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
