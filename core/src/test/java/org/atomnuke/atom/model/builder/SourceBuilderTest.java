package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Source;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.atomnuke.atom.model.ModelTestUtil.*;
import org.atomnuke.atom.model.impl.SourceImpl;

/**
 *
 * @author zinic
 */
@RunWith(Enclosed.class)
public class SourceBuilderTest {

   public static class WhenCopyingSourceObjects {

      @Test
      public void shouldCopyEmpty() {
         final SourceBuilder builder = new SourceBuilder(new SourceImpl());
         final Source copy = builder.build();

         assertNull(copy.base());
         assertNull(copy.generator());
         assertNull(copy.icon());
         assertNull(copy.id());
         assertNull(copy.logo());
         assertNull(copy.rights());
         assertNull(copy.subtitle());
         assertNull(copy.title());
         assertNull(copy.updated());

         assertTrue(copy.authors().isEmpty());
         assertTrue(copy.categories().isEmpty());
         assertTrue(copy.links().isEmpty());
      }

      @Test
      public void shouldCopy() {
         final Source original = newSource();
         final Source copy = new SourceBuilder(original).build();

         assertEquals(copy, original);
      }
   }
}