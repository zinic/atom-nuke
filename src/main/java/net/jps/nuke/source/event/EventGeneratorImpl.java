package net.jps.nuke.source.event;

import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.source.AtomSource;
import net.jps.nuke.source.AtomSourceException;
import net.jps.nuke.source.AtomSourceResult;
import net.jps.nuke.source.impl.AtomSourceResultImpl;

/**
 *
 * @author zinic
 */
public class EventGeneratorImpl implements AtomSource {

   private final boolean generateFeed;

   public EventGeneratorImpl(boolean generateFeed) {
      this.generateFeed = generateFeed;
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      if (generateFeed) {
         final FeedBuilder feedBuilder = FeedBuilder.newBuilder();

         return new AtomSourceResultImpl(feedBuilder.build());
      } else {
         final EntryBuilder entryBuilder = EntryBuilder.newBuilder();

         return new AtomSourceResultImpl(entryBuilder.build());
      }
   }
}
