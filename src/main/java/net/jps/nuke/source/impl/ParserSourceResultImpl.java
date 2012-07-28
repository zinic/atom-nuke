package net.jps.nuke.source.impl;

import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.source.AtomSourceResult;

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
