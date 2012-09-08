package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.impl.EntryImpl;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.atomnuke.atom.model.ModelTestUtil.*;

/**
 *
 * @author zinic
 */
@RunWith(Enclosed.class)
public class EntryBuilderTest {

   public static class WhenCopyingEntryObjects {

      @Test
      public void shouldCopyEmpty() {
         final EntryBuilder builder = new EntryBuilder(new EntryImpl());
         final Entry copy = builder.build();

         assertNull(copy.base());
         assertNull(copy.content());
         assertNull(copy.id());
         assertNull(copy.lang());
         assertNull(copy.published());
         assertNull(copy.rights());
         assertNull(copy.source());
         assertNull(copy.summary());
         assertNull(copy.title());
         assertNull(copy.updated());

         assertTrue(copy.authors().isEmpty());
         assertTrue(copy.categories().isEmpty());
         assertTrue(copy.contributors().isEmpty());
         assertTrue(copy.links().isEmpty());
      }

      @Test
      public void shouldCopy() {
         final Entry original = newEntry();
         final Entry copy = new EntryBuilder(original).build();

         assertEquals(copy, original);
      }
   }
}