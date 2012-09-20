package org.atomnuke.source.result;

import org.atomnuke.source.action.AtomSourceAction;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public class AtomSourceResultImpl implements AtomSourceResult {

   private final AtomSourceAction requestedAction;
   private final ResultType resultType;
   private final Feed feed;
   private final Entry entry;

   public AtomSourceResultImpl() {
      this(null, ResultType.EMPTY, null, null);
   }

   public AtomSourceResultImpl(Feed feed) {
      this(null, feed);
   }

   public AtomSourceResultImpl(Entry entry) {
      this(null, entry);
   }

   public AtomSourceResultImpl(AtomSourceAction action) {
      this(action, ResultType.EMPTY, null, null);
   }

   public AtomSourceResultImpl(AtomSourceAction action, Feed feed) {
      this(action, ResultType.FEED, feed, null);
   }

   public AtomSourceResultImpl(AtomSourceAction action, Entry entry) {
      this(action, ResultType.ENTRY, null, entry);
   }

   public AtomSourceResultImpl(AtomSourceAction requestedAction, ResultType resultType, Feed feed, Entry entry) {
      this.requestedAction = requestedAction;
      this.resultType = resultType;
      this.feed = feed;
      this.entry = entry;
   }

   @Override
   public ResultType type() {
      return resultType;
   }

   @Override
   public AtomSourceAction action() {
      return requestedAction;
   }

   @Override
   public boolean isEmpty() {
      return type() == ResultType.EMPTY;
   }

   @Override
   public boolean isFeedPage() {
      return type() == ResultType.FEED;
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
