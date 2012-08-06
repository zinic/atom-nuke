package org.atomnuke.atom.stax;

import org.atomnuke.atom.stax.StaxAtomWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import org.atomnuke.atom.ParserResult;
import org.atomnuke.atom.Reader;
import org.atomnuke.atom.Writer;
import org.atomnuke.atom.model.Category;
import org.atomnuke.atom.model.Content;
import org.atomnuke.atom.model.DateConstruct;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.atom.model.Generator;
import org.atomnuke.atom.model.Id;
import org.atomnuke.atom.model.Link;
import org.atomnuke.atom.model.PersonConstruct;
import org.atomnuke.atom.model.Source;
import org.atomnuke.atom.model.TextConstruct;
import org.atomnuke.atom.model.impl.LangAwareTextElement;
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

         final ParserResult rereadResult = reader.read(new ByteArrayInputStream(baos.toByteArray()));
         rereadFeed = rereadResult.getFeed();

         originalEntry = originalFeed.entries().get(0);

         for (Entry checkEntry : rereadFeed.entries()) {
            if (originalEntry.id().toString().equals(checkEntry.id().toString())) {
               rereadEntry = checkEntry;
               break;
            }
         }

         originalSource = originalEntry.source();
         rereadSource = rereadEntry.source();
      }

      public void assertGeneratorsAreEqual(Generator originalGenerator, Generator rereadGenerator) {
         assertNotNull(rereadGenerator.base());
         assertNotNull(rereadGenerator.lang());
         assertNotNull(rereadGenerator.uri());
         assertNotNull(rereadGenerator.toString());
         assertNotNull(rereadGenerator.version());

         assertEquals(originalGenerator.base(), rereadGenerator.base());
         assertEquals(originalGenerator.lang(), rereadGenerator.lang());
         assertEquals(originalGenerator.uri(), rereadGenerator.uri());
         assertEquals(originalGenerator.toString(), rereadGenerator.toString());
         assertEquals(originalGenerator.version(), rereadGenerator.version());
      }

      public void assertLangAwareTextElementsAreEqual(LangAwareTextElement originalLATE, LangAwareTextElement rereadLATE) {
         assertNotNull(rereadLATE.base());
         assertNotNull(rereadLATE.lang());
         assertNotNull(rereadLATE.toString());

         assertEquals(originalLATE.base(), rereadLATE.base());
         assertEquals(originalLATE.lang(), rereadLATE.lang());
         assertEquals(originalLATE.toString(), rereadLATE.toString());
      }

      public void assertIdsAreEqual(Id originalId, Id rereadId) {
         assertNotNull(rereadId.base());
         assertNotNull(rereadId.lang());
         assertNotNull(rereadId.toString());

         assertEquals(originalId.base(), rereadId.base());
         assertEquals(originalId.lang(), rereadId.lang());
         assertEquals(originalId.toString(), rereadId.toString());
      }

      public void assertTextConstructsAreEqual(TextConstruct originalTC, TextConstruct rereadTC) {
         assertNotNull(rereadTC.base());
         assertNotNull(rereadTC.lang());
         assertNotNull(rereadTC.type());
         assertNotNull(rereadTC.toString());

         assertEquals(originalTC.base(), rereadTC.base());
         assertEquals(originalTC.lang(), rereadTC.lang());
         assertEquals(originalTC.type(), rereadTC.type());
         assertEquals(originalTC.toString(), rereadTC.toString());
      }

      public void assertDateConstructsAreEqual(DateConstruct originalDC, DateConstruct rereadDC) {
         assertNotNull(rereadDC.base());
         assertNotNull(rereadDC.lang());
         assertNotNull(rereadDC.toString());
         assertNotNull(rereadDC.toCalendar());

         assertEquals(originalDC.base(), rereadDC.base());
         assertEquals(originalDC.lang(), rereadDC.lang());
         assertEquals(originalDC.toString(), rereadDC.toString());
         assertEquals(originalDC.toCalendar(), rereadDC.toCalendar());
      }

      public void assertLinksAreEqual(List<Link> originalLinks, List<Link> rereadLinks) {
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

      public void assertPersonConstructsAreEqual(List originalPCs, List rereadPCs) {
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

      public void assertCategoriesAreEqual(List<Category> originalCategories, List<Category> rereadCategories) {
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

         assertEquals(originalFeed.base(), rereadFeed.base());
         assertEquals(originalFeed.lang(), rereadFeed.lang());
      }

      @Test
      public void shouldWriteAuthors() {
         assertPersonConstructsAreEqual(originalFeed.authors(), rereadFeed.authors());
      }

      @Test
      public void shouldWriteLinks() {
         assertLinksAreEqual(originalFeed.links(), rereadFeed.links());
      }

      @Test
      public void shouldWriteContributors() {
         assertPersonConstructsAreEqual(originalFeed.contributors(), rereadFeed.contributors());
      }

      @Test
      public void shouldWriteCategories() {
         assertCategoriesAreEqual(originalFeed.categories(), rereadFeed.categories());
      }

      @Test
      public void shouldWriteUpdated() {
         assertDateConstructsAreEqual(originalFeed.updated(), rereadFeed.updated());
      }

      @Test
      public void shouldWriteRights() {
         assertTextConstructsAreEqual(originalFeed.rights(), rereadFeed.rights());
      }

      @Test
      public void shouldWriteTitle() {
         assertTextConstructsAreEqual(originalFeed.title(), rereadFeed.title());
      }

      @Test
      public void shouldWriteSubtitle() {
         assertTextConstructsAreEqual(originalFeed.subtitle(), rereadFeed.subtitle());
      }

      @Test
      public void shouldWriteId() {
         assertLangAwareTextElementsAreEqual((LangAwareTextElement) originalFeed.id(), (LangAwareTextElement) rereadFeed.id());
      }

      @Test
      public void shouldWriteLogo() {
         assertLangAwareTextElementsAreEqual((LangAwareTextElement) originalFeed.logo(), (LangAwareTextElement) rereadFeed.logo());
      }

      @Test
      public void shouldWriteIcon() {
         assertLangAwareTextElementsAreEqual((LangAwareTextElement) originalFeed.icon(), (LangAwareTextElement) rereadFeed.icon());
      }

      @Test
      public void shouldWriteGenerator() {
         assertGeneratorsAreEqual(originalFeed.generator(), rereadFeed.generator());
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

         assertEquals(originalEntry.base(), rereadEntry.base());
         assertEquals(originalEntry.lang(), rereadEntry.lang());
      }

      @Test
      public void shouldWriteLinks() {
         assertLinksAreEqual(originalEntry.links(), rereadEntry.links());
      }

      @Test
      public void shouldWriteAuthors() {
         assertPersonConstructsAreEqual(originalEntry.authors(), rereadEntry.authors());
      }

      @Test
      public void shouldWriteCategories() {
         assertCategoriesAreEqual(originalEntry.categories(), rereadEntry.categories());
      }

      @Test
      public void shouldWriteContributors() {
         assertPersonConstructsAreEqual(originalEntry.contributors(), rereadEntry.contributors());
      }

      @Test
      public void shouldWriteId() {
         assertLangAwareTextElementsAreEqual((LangAwareTextElement) originalEntry.id(), (LangAwareTextElement) originalEntry.id());
      }

      @Test
      public void shouldWriteLogo() {
         assertLangAwareTextElementsAreEqual((LangAwareTextElement) originalSource.logo(), (LangAwareTextElement) rereadSource.logo());
      }

      @Test
      public void shouldWriteIcon() {
         assertLangAwareTextElementsAreEqual((LangAwareTextElement) originalSource.icon(), (LangAwareTextElement) rereadSource.icon());
      }

      @Test
      public void shouldWriteRights() {
         assertTextConstructsAreEqual(originalEntry.rights(), rereadEntry.rights());
      }

      @Test
      public void shouldWriteSummary() {
         assertTextConstructsAreEqual(originalEntry.summary(), rereadEntry.summary());
      }

      @Test
      public void shouldWriteTitle() {
         assertTextConstructsAreEqual(originalEntry.title(), rereadEntry.title());
      }

      @Test
      public void shouldWritePublished() {
         assertDateConstructsAreEqual(originalEntry.published(), rereadEntry.published());
      }

      @Test
      public void shouldWriteUpdated() {
         assertDateConstructsAreEqual(originalEntry.updated(), rereadEntry.updated());
      }

      @Test
      public void shouldWriteContent() {
         final Content originalContent = originalEntry.content(), rereadContent = rereadEntry.content();

         assertNotNull(rereadContent.base());
         assertNotNull(rereadContent.lang());
         assertNotNull(rereadContent.src());
         assertNotNull(rereadContent.type());
         assertNotNull(rereadContent.toString());

         assertEquals(originalContent.base(), rereadContent.base());
         assertEquals(originalContent.lang(), rereadContent.lang());
         assertEquals(originalContent.src(), rereadContent.src());
         assertEquals(originalContent.type(), rereadContent.type());
         assertEquals(originalContent.toString(), rereadContent.toString());
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

         assertEquals(originalSource.base(), rereadSource.base());
         assertEquals(originalSource.lang(), rereadSource.lang());
      }

      @Test
      public void shouldWriteAuthors() {
         assertPersonConstructsAreEqual(originalSource.authors(), rereadSource.authors());
      }

      @Test
      public void shouldWriteLinks() {
         assertLinksAreEqual(originalSource.links(), rereadSource.links());
      }

      @Test
      public void shouldWriteCategories() {
         assertCategoriesAreEqual(originalSource.categories(), rereadSource.categories());
      }

      @Test
      public void shouldWriteUpdated() {
         assertDateConstructsAreEqual(originalSource.updated(), rereadSource.updated());
      }

      @Test
      public void shouldWriteRights() {
         assertTextConstructsAreEqual(originalSource.rights(), rereadSource.rights());
      }

      @Test
      public void shouldWriteTitle() {
         assertTextConstructsAreEqual(originalSource.title(), rereadSource.title());
      }

      @Test
      public void shouldWriteSubtitle() {
         assertTextConstructsAreEqual(originalSource.subtitle(), rereadSource.subtitle());
      }

      @Test
      public void shouldWriteId() {
         assertLangAwareTextElementsAreEqual((LangAwareTextElement) originalSource.id(), (LangAwareTextElement) rereadSource.id());
      }

      @Test
      public void shouldWriteLogo() {
         assertLangAwareTextElementsAreEqual((LangAwareTextElement) originalSource.logo(), (LangAwareTextElement) rereadSource.logo());
      }

      @Test
      public void shouldWriteIcon() {
         assertLangAwareTextElementsAreEqual((LangAwareTextElement) originalSource.icon(), (LangAwareTextElement) rereadSource.icon());
      }

      @Test
      public void shouldWriteGenerator() {
         assertGeneratorsAreEqual(originalSource.generator(), rereadSource.generator());
      }
   }
}
