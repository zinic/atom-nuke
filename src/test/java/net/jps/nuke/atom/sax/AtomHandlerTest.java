package net.jps.nuke.atom.sax;

import net.jps.nuke.atom.sax.impl.SaxAtomParser;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Calendar;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
import net.jps.nuke.atom.Result;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.atom.model.Link;
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
public class AtomHandlerTest {

   private static final Calendar THAT_DATE = DatatypeConverter.parseDate("2002-05-30T09:30:10Z");

   public static InputStream openFeedResource(String name) throws IOException {
      return AtomHandlerTest.class.getResourceAsStream("/META-INF/examples/atom/feed/" + name);
   }

   public static InputStream openEntryResource(String name) throws IOException {
      return AtomHandlerTest.class.getResourceAsStream("/META-INF/examples/atom/entry/" + name);
   }

   @Ignore
   public static class TestParent {

      private SaxAtomParser parserInstance;

      @Before
      public void standUp() {
         parserInstance = new SaxAtomParser();
      }

      protected SaxAtomParser getParser() {
         return parserInstance;
      }

      protected void checkAuthors(List<Author> authors) {
         assertEquals(3, authors.size());

         for (int i = 0; i < authors.size(); i++) {
            assertEquals("John Doe", authors.get(i).name());
            assertEquals("http://john.doe.example.domain/", authors.get(i).uri());
            assertEquals("john.doe@example.domain", authors.get(i).email());
         }
      }

      protected void checkContributors(List<Contributor> contributors) {
         assertEquals(3, contributors.size());

         for (int i = 0; i < contributors.size(); i++) {
            assertEquals("John Doe", contributors.get(i).name());
            assertEquals("http://john.doe.example.domain/", contributors.get(i).uri());
            assertEquals("john.doe@example.domain", contributors.get(i).email());
         }
      }

      protected void checkCategories(List<Category> categories) {
         assertEquals(3, categories.size());

         for (int i = 0; i < categories.size(); i++) {
            final Category c = categories.get(i);

            assertEquals("a", c.term());
            assertEquals("Category A", c.label());
            assertEquals("http", c.scheme());
         }
      }

      protected void checkLinks(List<Link> links) {
         assertEquals(3, links.size());

         for (int i = 0; i < links.size(); i++) {
            final Link l = links.get(i);

            assertEquals("http://example.domain/12345", l.href());
            assertEquals("en", l.hreflang());
            assertTrue(52153 == l.length());
            assertEquals("self", l.rel());
            assertEquals("me", l.title());
            assertEquals("application/atom+xml", l.type());
         }
      }
   }

   public static class WhenParsingFeedElements extends TestParent {

      @Test
      public void shouldReturnEmptyFeed() throws Exception {
         final String feedXml = "<feed />";
         final Result result = getParser().read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getFeed());
      }

      @Test
      public void shouldReturnFeedWithEmptyEntry() throws Exception {
         final String feedXml = "<feed><entry></entry></feed>";
         final Result result = getParser().read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getFeed());
         assertTrue(1 == result.getFeed().entries().size());
      }

      @Test
      public void shouldReadFeedAttributes() throws Exception {
         final String feedXml = "<feed lang='en' base='uri'></feed>";
         final Result result = getParser().read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getFeed());
         assertEquals("en", result.getFeed().lang());
         assertEquals(URI.create("uri"), result.getFeed().base());
      }

      @Test
      public void shouldReadFeedAuthors() throws Exception {
         final Result result = getParser().read(openFeedResource("FeedWithAuthors.xml"));

         final Feed f = result.getFeed();
         checkAuthors(f.authors());
      }

      @Test
      public void shouldReadFeedContributors() throws Exception {
         final Result result = getParser().read(openFeedResource("FeedWithContributors.xml"));

         final Feed f = result.getFeed();
         assertEquals(3, f.contributors().size());
         checkContributors(f.contributors());
      }

      @Test
      public void shouldReadFeedCategories() throws Exception {
         final Result result = getParser().read(openFeedResource("FeedWithCategories.xml"));

         final Feed f = result.getFeed();
         checkCategories(f.categories());
      }

      @Test
      public void shouldReadFeedID() throws Exception {
         final Result result = getParser().read(openFeedResource("FeedWithID.xml"));

         final Feed f = result.getFeed();
         assertEquals("urn:uuid:05b6e287-eb46-4f4b-a1a7-1a171ea66c78", f.id().value());
      }

      @Test
      public void shouldReadFeedIcon() throws Exception {
         final Result result = getParser().read(openFeedResource("FeedWithIcon.xml"));

         final Feed f = result.getFeed();
         assertEquals("http://example.domain/images/icon.jpg", f.icon().value());
      }

      @Test
      public void shouldReadFeedLogo() throws Exception {
         final Result result = getParser().read(openFeedResource("FeedWithLogo.xml"));

         final Feed f = result.getFeed();
         assertEquals("http://example.domain/images/logo.jpg", f.logo().value());
      }

      @Test
      public void shouldReadFeedLinks() throws Exception {
         final Result result = getParser().read(openFeedResource("FeedWithLinks.xml"));

         final Feed f = result.getFeed();
         checkLinks(f.links());
      }

      @Test
      public void shouldReadFeedUpdated() throws Exception {
         final Result result = getParser().read(openFeedResource("FeedWithUpdated.xml"));

         final Feed f = result.getFeed();
         assertEquals(THAT_DATE, f.updated().asCalendar());
      }

      @Test
      public void shouldReadFeedRights() throws Exception {
         final Result result = getParser().read(openFeedResource("FeedWithRights.xml"));

         final Feed f = result.getFeed();
         assertEquals("Rights.", f.rights().value());
      }
   }

   public static class WhenParsingEntryElements extends TestParent {

      @Test
      public void shouldReturnEmptyEntry() throws Exception {
         final String feedXml = "<entry />";
         final Result result = getParser().read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getEntry());
      }

      @Test
      public void shouldReturnFeedWithEmptyEntry() throws Exception {
         final String feedXml = "<feed><entry /></feed>";
         final Result result = getParser().read(new ByteArrayInputStream(feedXml.getBytes()));

         final Feed f = result.getFeed();

         assertNotNull(f);
         assertEquals(1, f.entries().size());
      }

      @Test
      public void shouldReadEntryAttributes() throws Exception {
         final String feedXml = "<feed><entry lang='en' base='uri' /></feed>";
         final Result result = getParser().read(new ByteArrayInputStream(feedXml.getBytes()));

         final Feed f = result.getFeed();

         assertNotNull(result.getFeed());
         assertEquals(1, result.getFeed().entries().size());

         final Entry e = f.entries().get(0);

         assertEquals("en", e.lang());
         assertEquals(URI.create("uri"), e.base());
      }

      @Test
      public void shouldReadEntryAuthors() throws Exception {
         final Result result = getParser().read(openEntryResource("EntryWithAuthors.xml"));

         final Entry e = result.getEntry();
         checkAuthors(e.authors());
      }

      @Test
      public void shouldReadEntryContributors() throws Exception {
         final Result result = getParser().read(openEntryResource("EntryWithContributors.xml"));

         final Entry e = result.getEntry();
         assertEquals(3, e.contributors().size());
         checkContributors(e.contributors());
      }

      @Test
      public void shouldReadEntryContent() throws Exception {
         final Result result = getParser().read(openEntryResource("EntryWithContent.xml"));

         final Entry e = result.getEntry();
         assertEquals("Text content.", e.content().value());
      }

      @Test
      public void shouldReadEntryCategories() throws Exception {
         final Result result = getParser().read(openEntryResource("EntryWithCategories.xml"));

         final Entry e = result.getEntry();
         checkCategories(e.categories());
      }

      @Test
      public void shouldReadEntryID() throws Exception {
         final Result result = getParser().read(openEntryResource("EntryWithID.xml"));

         final Entry e = result.getEntry();
         assertEquals("urn:uuid:05b6e287-eb46-4f4b-a1a7-1a171ea66c78", e.id().value());
      }

      @Test
      public void shouldReadEntryLinks() throws Exception {
         final Result result = getParser().read(openEntryResource("EntryWithLinks.xml"));

         final Entry e = result.getEntry();
         checkLinks(e.links());
      }

      @Test
      public void shouldReadEntryUpdated() throws Exception {
         final Result result = getParser().read(openEntryResource("EntryWithUpdated.xml"));

         final Entry e = result.getEntry();
         assertEquals(THAT_DATE, e.updated().asCalendar());
      }

      @Test
      public void shouldReadEntryPublished() throws Exception {
         final Result result = getParser().read(openEntryResource("EntryWithPublished.xml"));

         final Entry e = result.getEntry();
         assertEquals(THAT_DATE, e.published().asCalendar());
      }

      @Test
      public void shouldReadEntryRights() throws Exception {
         final Result result = getParser().read(openEntryResource("EntryWithRights.xml"));

         final Entry e = result.getEntry();
         assertEquals("<this attr\"test\"><is>some</is></this>", e.rights().value());
      }

      @Test
      public void shouldReadEntrySource() throws Exception {
         final Result result = getParser().read(openEntryResource("EntryWithSource.xml"));

         final Entry e = result.getEntry();
      }
   }
}
