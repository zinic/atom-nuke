package org.atomnuke.examples.source;

import java.util.concurrent.atomic.AtomicLong;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.atom.model.builder.AuthorBuilder;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.atom.model.builder.FeedBuilder;
import org.atomnuke.atom.model.builder.IdBuilder;
import org.atomnuke.atom.model.builder.TitleBuilder;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.AtomSourceResult;
import org.atomnuke.source.impl.AtomSourceResultImpl;
import org.atomnuke.task.context.TaskContext;

/**
 *
 * @author zinic
 */
public class EventGenerator implements AtomSource {

   private final boolean generateFeed, generateForever;
   private final String generatorPrefix;
   private final AtomicLong remainingEvents;

   public EventGenerator() {
      this(0, "", true, true);
   }

   public EventGenerator(String generatorPrefix, boolean generateFeed) {
      this(0, generatorPrefix, generateFeed, true);
   }

   public EventGenerator(long remainingEvents, String generatorPrefix, boolean generateFeed) {
      this(0, generatorPrefix, generateFeed, false);
   }

   private EventGenerator(long remainingEvents, String generatorPrefix, boolean generateFeed, boolean generateForever) {
      this.remainingEvents = new AtomicLong(remainingEvents);
      this.generatorPrefix = generatorPrefix;
      this.generateFeed = generateFeed;
      this.generateForever = generateForever;
   }

   @Override
   public void init(TaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy(TaskContext tc) throws DestructionException {
   }

   private Entry buildEntry(String id) {
      final EntryBuilder entry = new EntryBuilder();

      final IdBuilder idBuilder = new IdBuilder();
      idBuilder.appendValue(id);
      entry.setId(idBuilder.build());

      final CategoryBuilder testCat = new CategoryBuilder();
      testCat.setTerm("test");
      entry.addCategory(testCat.build());

      if (Math.random() > 0.5) {
         final CategoryBuilder otherCat = new CategoryBuilder();
         otherCat.setTerm("other-cat");
         entry.addCategory(otherCat.build());
      }

      return entry.build();
   }

   private Feed buildFeed() {
      final FeedBuilder feed = new FeedBuilder();

      final TitleBuilder title = new TitleBuilder();
      title.appendValue(generatorPrefix).appendValue(" Example Feed");
      feed.setTitle(title.build());

      final AuthorBuilder author = new AuthorBuilder();
      author.setName("Author");
      feed.addAuthor(author.build());

      final CategoryBuilder category = new CategoryBuilder();
      category.setTerm("test");
      feed.addCategory(category.build());

      for (int entryNum = 1; entryNum <= 50; entryNum++) {
         feed.addEntry(buildEntry("urn:entryid:" + generatorPrefix + "-" + entryNum));
      }

      return feed.build();
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      if (!generateForever && remainingEvents.decrementAndGet() < 0) {
         throw new AtomSourceException("Out of messages!");
      }

      if (generateFeed) {
         return new AtomSourceResultImpl(buildFeed());
      } else {
         return new AtomSourceResultImpl(buildEntry("urn:entryid:" + generatorPrefix + "-0"));
      }
   }
}
