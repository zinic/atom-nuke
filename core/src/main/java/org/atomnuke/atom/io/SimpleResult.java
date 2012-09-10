package org.atomnuke.atom.io;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public class SimpleResult implements ReaderResult {

   private final Feed feedResult;
   private final Entry entryResult;

   public SimpleResult(Entry e) {
      this(null, e);
   }

   public SimpleResult(Feed f) {
      this(f, null);
   }

   private SimpleResult(Feed feedResult, Entry entryResult) {
      this.feedResult = feedResult;
      this.entryResult = entryResult;
   }

   @Override
   public boolean isEntry() {
      return entryResult != null;
   }

   @Override
   public boolean isFeed() {
      return feedResult != null;
   }

   @Override
   public Feed getFeed() {
      return feedResult;
   }

   @Override
   public Entry getEntry() {
      return entryResult;
   }
}
