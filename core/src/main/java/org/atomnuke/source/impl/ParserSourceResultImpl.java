package org.atomnuke.source.impl;

import org.atomnuke.atom.ParserResult;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.source.AtomSourceResult;

/**
 *
 * @author zinic
 */
public class ParserSourceResultImpl implements AtomSourceResult {

   private final ParserResult readResult;

   public ParserSourceResultImpl(ParserResult readResult) {
      this.readResult = readResult;
   }

   @Override
   public boolean isFeedPage() {
      return readResult.getFeed() != null;
   }

   @Override
   public Feed feed() {
      return readResult.getFeed();
   }

   @Override
   public Entry entry() {
      return readResult.getEntry();
   }
}
