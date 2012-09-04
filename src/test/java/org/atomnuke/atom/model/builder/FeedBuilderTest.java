package org.atomnuke.atom.model.builder;

import java.net.URI;
import org.atomnuke.atom.model.Feed;
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
public class FeedBuilderTest {

   public static class WhenCopyingFeedObjects {

      @Test
      public void shouldCopy() {
         final FeedBuilder builder = new FeedBuilder();

         builder.setBase(URI.create("/uri")).setLang("en");
         builder.setGenerator(newGenerator()).setIcon(newIcon()).setId(newId()).setLogo(newLogo());
         builder.setRights(newRights()).setSubtitle(newSubtitle()).setTitle(newTitle()).setUpdated(newUpdated());

         final Feed original = builder.build();
         final Feed copy = new FeedBuilder(original).build();

         // TODO:Test - Finish asserts beyong my own little sanity check
         assertEquals(copy.generator().uri(), original.generator().uri());
         assertEquals(copy.generator().version(), original.generator().version());
         assertEquals(copy.generator().base(), original.generator().base());
         assertEquals(copy.generator().lang(), original.generator().lang());
         assertEquals(copy.generator().toString(), original.generator().toString());
      }
   }
}
