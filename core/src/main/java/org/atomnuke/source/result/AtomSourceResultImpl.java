package org.atomnuke.source.result;

import org.atomnuke.source.action.AtomSourceAction;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.source.action.ActionType;
import org.atomnuke.source.action.AtomSourceActionImpl;

/**
 *
 * @author zinic
 */
public class AtomSourceResultImpl implements AtomSourceResult {

   private static final AtomSourceAction DEFAULT_SOURCE_ACTION = new AtomSourceActionImpl(ActionType.OK);

   private final AtomSourceAction requestedAction;
   private final ResultType resultType;
   private final Feed feed;
   private final Entry entry;

   public AtomSourceResultImpl(Feed feed) {
      this(DEFAULT_SOURCE_ACTION, feed);
   }

   public AtomSourceResultImpl(Entry entry) {
      this(DEFAULT_SOURCE_ACTION, entry);
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
   public Feed feed() {
      return feed;
   }

   @Override
   public Entry entry() {
      return entry;
   }
}
