package net.jps.nuke.atom.stax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.Writer;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.atom.model.Generator;
import net.jps.nuke.atom.model.Link;
import net.jps.nuke.atom.model.PersonConstruct;
import net.jps.nuke.atom.sax.impl.SaxAtomParser;
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

      @Before
      public void beforeAny() throws Exception {
         final Reader reader = new SaxAtomParser();
         final ParserResult originalResult = reader.read(AtomWriterTest.class.getResourceAsStream("/META-INF/examples/atom/PerformanceTestContents.xml"));

         originalFeed = originalResult.getFeed();

         final Writer writer = new StaxAtomWriter();
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();

         writer.write(baos, originalFeed);

         final ParserResult rereadResult = reader.read(new ByteArrayInputStream(baos.toByteArray()));
         rereadFeed = rereadResult.getFeed();
      }

      public void assertLinksAreEqual(List<Link> originalLinks, List<Link> rereadLinks) throws Exception {
         assertEquals(originalLinks.size(), rereadLinks.size());

         for (Link originalLink : originalLinks) {
            for (Iterator<Link> linkItr = rereadLinks.iterator(); linkItr.hasNext();) {
               final Link rereadLink = linkItr.next();

               if (originalLink.href().equals(rereadLink.href())) {
                  linkItr.remove();

                  assertNotNull(rereadLink.hreflang());
                  assertNotNull(rereadLink.length());
                  assertNotNull(rereadLink.rel());
                  assertNotNull(rereadLink.title());
                  assertNotNull(rereadLink.type());
                  assertNotNull(rereadLink.lang());
                  assertNotNull(rereadLink.base());

                  assertEquals(originalLink.hreflang(), rereadLink.hreflang());
                  assertEquals(originalLink.length(), rereadLink.length());
                  assertEquals(originalLink.rel(), rereadLink.rel());
                  assertEquals(originalLink.title(), rereadLink.title());
                  assertEquals(originalLink.type(), rereadLink.type());
                  assertEquals(originalLink.lang(), rereadLink.lang());
                  assertEquals(originalLink.base(), rereadLink.base());
                  break;
               }
            }
         }

         assertTrue("All links must match original set.", rereadLinks.isEmpty());
      }

      public void assertPersonConstructsAreEqual(List originalPCs, List rereadPCs) throws Exception {
         assertEquals(originalPCs.size(), rereadPCs.size());

         for (PersonConstruct originalPC : (List<PersonConstruct>) originalPCs) {
            for (Iterator<PersonConstruct> pcItr = rereadPCs.iterator(); pcItr.hasNext();) {
               final PersonConstruct rereadPC = pcItr.next();

               if (originalPC.name().equals(rereadPC.name())) {
                  pcItr.remove();

                  assertNotNull(rereadPC.base());
                  assertNotNull(rereadPC.lang());
                  assertNotNull(rereadPC.email());
                  assertNotNull(rereadPC.uri());

                  assertEquals(originalPC.email(), rereadPC.email());
                  assertEquals(originalPC.uri(), rereadPC.uri());
                  assertEquals(originalPC.lang(), rereadPC.lang());
                  assertEquals(originalPC.base(), rereadPC.base());
                  break;
               }
            }
         }

         assertTrue("All person constructs must match original set.", rereadPCs.isEmpty());
      }

      public void assertCategoriesAreEqual(List<Category> originalCategories, List<Category> rereadCategories) throws Exception {
         assertEquals(originalCategories.size(), rereadCategories.size());

         for (Category originalCategory : originalCategories) {
            for (Iterator<Category> categoryItr = rereadCategories.iterator(); categoryItr.hasNext();) {
               final Category rereadCategory = categoryItr.next();

               if (originalCategory.label().equals(rereadCategory.label())) {
                  categoryItr.remove();

                  assertNotNull(rereadCategory.base());
                  assertNotNull(rereadCategory.lang());
                  assertNotNull(rereadCategory.label());
                  assertNotNull(rereadCategory.scheme());
                  assertNotNull(rereadCategory.term());

                  assertEquals(originalCategory.lang(), rereadCategory.lang());
                  assertEquals(originalCategory.base(), rereadCategory.base());
                  assertEquals(originalCategory.scheme(), rereadCategory.scheme());
                  assertEquals(originalCategory.term(), rereadCategory.term());
                  assertEquals(originalCategory.label(), rereadCategory.label());
                  break;
               }
            }
         }

         assertTrue("All categories must match original set.", rereadCategories.isEmpty());
      }
   }

   public static class WhenWritingFeeds extends TestParent {

      @Test
      public void shouldWriteAuthors() throws Exception {
         assertPersonConstructsAreEqual(originalFeed.authors(), rereadFeed.authors());
      }

      @Test
      public void shouldWriteLinks() throws Exception {
         assertLinksAreEqual(originalFeed.links(), rereadFeed.links());
      }

      @Test
      public void shouldWriteContributors() throws Exception {
         assertPersonConstructsAreEqual(originalFeed.contributors(), rereadFeed.contributors());
      }

      @Test
      public void shouldWriteCategories() throws Exception {
         assertCategoriesAreEqual(originalFeed.categories(), rereadFeed.categories());
      }

      @Test
      public void shouldWriteGenerator() throws Exception {
         final Generator originalGenerator = originalFeed.generator(), rereadGenerator = rereadFeed.generator();

         assertNotNull(rereadGenerator.base());
         assertNotNull(rereadGenerator.lang());
         assertNotNull(rereadGenerator.uri());
         assertNotNull(rereadGenerator.value());
         assertNotNull(rereadGenerator.version());

         assertEquals(originalGenerator.base(), rereadGenerator.base());
         assertEquals(originalGenerator.lang(), rereadGenerator.lang());
         assertEquals(originalGenerator.uri(), rereadGenerator.uri());
         assertEquals(originalGenerator.value(), rereadGenerator.value());
         assertEquals(originalGenerator.version(), rereadGenerator.version());
      }
   }

   public static class WhenWritingEntries extends TestParent {

      protected Entry originalEntry, rereadEntry;

      @Before
      public void standUp() {
         originalEntry = originalFeed.entries().get(0);

         for (Entry checkEntry : rereadFeed.entries()) {
            if (originalEntry.id().value().equals(checkEntry.id().value())) {
               rereadEntry = checkEntry;
               break;
            }
         }
      }

      @Test
      public void shouldWriteLinks() throws Exception {
         assertLinksAreEqual(originalEntry.links(), rereadEntry.links());
      }

      @Test
      public void shouldWriteAuthors() throws Exception {
         assertPersonConstructsAreEqual(originalEntry.authors(), rereadEntry.authors());
      }

      @Test
      public void shouldWriteCategories() throws Exception {
         assertCategoriesAreEqual(originalEntry.categories(), rereadEntry.categories());
      }

      @Test
      public void shouldWriteContributors() throws Exception {
         assertPersonConstructsAreEqual(originalEntry.contributors(), rereadEntry.contributors());
      }
   }

   @Ignore
   public static class WhenWritingSources extends TestParent {
   }
}
