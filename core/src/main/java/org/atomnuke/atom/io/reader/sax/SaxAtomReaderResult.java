package org.atomnuke.atom.io.reader.sax;

import org.atomnuke.atom.io.ReaderResult;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public class SaxAtomReaderResult implements ReaderResult {

   private Feed feedResult;
   private Entry entryResult;

   public void setFeedResult(Feed feedResult) {
      this.feedResult = feedResult;
   }

   public void setEntryResult(Entry entryResult) {
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
