package net.jps.nuke.examples.source;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
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
   private final String generatorPrefix;

   public EventGenerator(String generatorPrefix, boolean generateFeed) {
      this.generatorPrefix = generatorPrefix;
      this.generateFeed = generateFeed;
   }

   private Entry buildEntry(String id) {
      final EntryBuilder entry = new EntryBuilder();

      final IdBuilder idBuilder = new IdBuilder();
      idBuilder.appendValue(id);
      entry.setId(idBuilder);

      final CategoryBuilder testCat = new CategoryBuilder();
      testCat.setTerm("test");
      entry.addCategory(testCat);

      if (Math.random() > 0.5) {
         final CategoryBuilder otherCat = new CategoryBuilder();
         otherCat.setTerm("other-cat");
         entry.addCategory(otherCat);
      }

      return entry;
   }

   private Feed buildFeed() {
      final FeedBuilder feed = new FeedBuilder();

      final TitleBuilder title = new TitleBuilder();
      title.getValueBuilder().append(generatorPrefix);
      title.getValueBuilder().append(" Example Feed");
      feed.setTitle(title);

      final AuthorBuilder author = new AuthorBuilder();
      author.setName("Author");
      feed.addAuthor(author);

      final CategoryBuilder category = new CategoryBuilder();
      category.setTerm("test");
      feed.addCategory(category);

      for (int entryNum = 1; entryNum <= 50; entryNum++) {
         feed.addEntry(buildEntry("urn:entryid:" + generatorPrefix + "-" + entryNum));
      }

      return feed;
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      if (generateFeed) {
         return new AtomSourceResultImpl(buildFeed());
      } else {
         return new AtomSourceResultImpl(buildEntry("urn:entryid:" + generatorPrefix + "-0"));
      }
   }
}
