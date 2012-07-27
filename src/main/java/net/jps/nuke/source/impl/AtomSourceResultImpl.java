package net.jps.nuke.source.impl;

import net.jps.nuke.atom.Result;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.source.AtomSourceResult;

/**
 *
 * @author zinic
 */
public class AtomSourceResultImpl implements AtomSourceResult {

   private final Result readResult;

   public AtomSourceResultImpl(Result readResult) {
      this.readResult = readResult;
   }

   public boolean isFeedPage() {
      return readResult.getFeed() != null;
   }

   public Feed feed() {
      return readResult.getFeed();
   }

   public Entry entry() {
      return readResult.getEntry();
   }
}
