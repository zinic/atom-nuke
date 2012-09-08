package org.atomnuke.atom.stax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.atomnuke.atom.ParserResult;
import org.atomnuke.atom.Reader;
import org.atomnuke.atom.Writer;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.atom.model.Source;
import org.atomnuke.atom.sax.impl.SaxAtomParser;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;

/**
 *
 * @author zinic
 */
@RunWith(Enclosed.class)
public class AtomWriterTest {

   @Ignore
   public static class TestParent {

      protected Feed originalFeed, rereadFeed;
      protected Entry originalEntry, rereadEntry;
      protected Source originalSource, rereadSource;

      @Before
      public void beforeAny() throws Exception {
         final Reader reader = new SaxAtomParser();
         final ParserResult originalResult = reader.read(AtomWriterTest.class.getResourceAsStream("/META-INF/examples/atom/PerformanceTestContents.xml"));

         originalFeed = originalResult.getFeed();

         final Writer writer = new StaxAtomWriter();
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();

         writer.write(baos, originalFeed);
         rereadFeed = reader.read(new ByteArrayInputStream(baos.toByteArray())).getFeed();

         baos.reset();

         originalEntry = originalFeed.entries().get(0);

         writer.write(baos, originalEntry);
         rereadEntry = reader.read(new ByteArrayInputStream(baos.toByteArray())).getEntry();

         for (Entry checkEntry : originalFeed.entries()) {
            if (rereadEntry.equals(checkEntry)) {
               originalEntry = checkEntry;
               break;
            }
         }

         originalSource = originalEntry.source();
         rereadSource = rereadEntry.source();
      }
   }

   public static class WhenWritingFeeds extends TestParent {

      @Test
      public void shouldWriteFeeds() {
         assertNotNull(rereadFeed.base());
         assertNotNull(rereadFeed.generator());
         assertNotNull(rereadFeed.icon());
         assertNotNull(rereadFeed.id());
         assertNotNull(rereadFeed.lang());
         assertNotNull(rereadFeed.logo());
         assertNotNull(rereadFeed.rights());
         assertNotNull(rereadFeed.subtitle());
         assertNotNull(rereadFeed.title());
         assertNotNull(rereadFeed.updated());

         assertEquals(originalFeed, rereadFeed);
      }
   }

   public static class WhenWritingEntries extends TestParent {

      @Test
      public void shouldWriteEntries() {
         assertNotNull(rereadEntry.base());
         assertNotNull(rereadEntry.content());
         assertNotNull(rereadEntry.id());
         assertNotNull(rereadEntry.lang());
         assertNotNull(rereadEntry.published());
         assertNotNull(rereadEntry.rights());
         assertNotNull(rereadEntry.source());
         assertNotNull(rereadEntry.summary());
         assertNotNull(rereadEntry.title());
         assertNotNull(rereadEntry.updated());

         assertEquals(originalEntry, rereadEntry);
      }
   }

   public static class WhenWritingSources extends TestParent {

      @Test
      public void shouldWriteSources() {
         assertNotNull(rereadSource.base());
         assertNotNull(rereadSource.generator());
         assertNotNull(rereadSource.icon());
         assertNotNull(rereadSource.id());
         assertNotNull(rereadSource.lang());
         assertNotNull(rereadSource.logo());
         assertNotNull(rereadSource.rights());
         assertNotNull(rereadSource.subtitle());
         assertNotNull(rereadSource.title());
         assertNotNull(rereadSource.updated());

         assertEquals(originalSource, rereadSource);
      }
   }
}
