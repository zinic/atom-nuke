package org.atomnuke.atom.model.builder;

import java.net.URI;
import org.atomnuke.atom.model.Feed;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.atomnuke.atom.model.ModelTestUtil.*;
import org.atomnuke.atom.model.impl.FeedImpl;

/**
 *
 * @author zinic
 */
@RunWith(Enclosed.class)
public class FeedBuilderTest {

   public static class WhenCopyingFeedObjects {

      @Test
      public void shouldCopyEmpty() {
         final FeedBuilder builder = new FeedBuilder(new FeedImpl());
         final Feed copy = builder.build();

         assertNull(copy.base());
         assertNull(copy.generator());
         assertNull(copy.icon());
         assertNull(copy.id());
         assertNull(copy.lang());;
         assertNull(copy.logo());
         assertNull(copy.rights());
         assertNull(copy.subtitle());
         assertNull(copy.title());
         assertNull(copy.updated());

         assertTrue(copy.entries().isEmpty());
         assertTrue(copy.authors().isEmpty());
         assertTrue(copy.categories().isEmpty());
         assertTrue(copy.contributors().isEmpty());
         assertTrue(copy.links().isEmpty());
      }

      @Test
      public void shouldCopy() {
         final FeedBuilder builder = new FeedBuilder();

         builder.setBase(URI.create("/uri")).setLang("en");

         builder.setGenerator(newGenerator());
         builder.setIcon(newIcon());
         builder.setId(newId());
         builder.setLogo(newLogo());
         builder.setRights(newRights());
         builder.setSubtitle(newSubtitle());
         builder.setTitle(newTitle());
         builder.setUpdated(newUpdated());

         builder.addEntry(newEntry());
         builder.addAuthor(newAuthor());
         builder.addCategory(newCategory());
         builder.addContributor(newContributor());
         builder.addLink(newLink());

         final Feed original = builder.build();
         final Feed copy = new FeedBuilder(original).build();

         assertEquals(copy, original);
      }
   }
}
