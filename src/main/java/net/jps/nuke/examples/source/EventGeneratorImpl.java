package net.jps.nuke.examples.source;

import net.jps.nuke.atom.model.builder.AuthorBuilder;
import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.model.builder.TitleBuilder;
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
         final FeedBuilder feed = new FeedBuilder();

         final TitleBuilder title = new TitleBuilder();
         title.getValueBuilder().append("Example Feed");
         feed.setTitle(title);
         
         final AuthorBuilder author = new AuthorBuilder();
         author.setName("Author");
         feed.addAuthor(author);

         return new AtomSourceResultImpl(feed);
      } else {
         final EntryBuilder entryBuilder = new EntryBuilder();

         return new AtomSourceResultImpl(entryBuilder);
      }
   }
}
