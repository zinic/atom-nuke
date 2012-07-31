package net.jps.nuke.examples.source;

import net.jps.nuke.atom.model.Id;
import net.jps.nuke.atom.model.builder.AuthorBuilder;
import net.jps.nuke.atom.model.builder.CategoryBuilder;
import net.jps.nuke.atom.model.builder.EntryBuilder;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.model.builder.IdBuilder;
import net.jps.nuke.atom.model.builder.TitleBuilder;
import net.jps.nuke.source.AtomSource;
import net.jps.nuke.source.AtomSourceException;
import net.jps.nuke.source.AtomSourceResult;
import net.jps.nuke.source.impl.AtomSourceResultImpl;

/**
 *
 * @author zinic
 */
public class EventGenerator implements AtomSource {

   private final boolean generateFeed;

   public EventGenerator(boolean generateFeed) {
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

         final CategoryBuilder category = new CategoryBuilder();
         category.setTerm("test");
         feed.addCategory(category);

         for (int entryNum = 1; entryNum <= 50; entryNum++) {
            final EntryBuilder entry = new EntryBuilder();
            
            final IdBuilder id = new IdBuilder();
            id.appendValue("urn:" + entryNum + "-entryId");
            entry.setId(id);
            
            final CategoryBuilder entryCategory = new CategoryBuilder();
            entryCategory.setTerm("test");
            entry.addCategory(entryCategory);
            
            feed.addEntry(entry);
         }

         return new AtomSourceResultImpl(feed);
      } else {
         final EntryBuilder entryBuilder = new EntryBuilder();

         return new AtomSourceResultImpl(entryBuilder);
      }
   }
}
