package net.jps.nuke.atom.sax;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Category;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
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
   }

   public static class WhenParsingFeedElements extends TestParent {

      @Test
      public void shouldReturnEmptyFeed() throws Exception {
         final String feedXml = "<feed />";
         final ParserResult result = getParser().read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getFeed());
      }

      @Test
      public void shouldReadFeedAttributes() throws Exception {
         final String feedXml = "<feed lang='en' base='uri'></feed>";
         final ParserResult result = getParser().read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getFeed());
         assertEquals("en", result.getFeed().lang());
         assertEquals(URI.create("uri"), result.getFeed().base());
      }

      @Test
      public void shouldReadFeedAuthors() throws Exception {
         final ParserResult result = getParser().read(openFeedResource("FeedWithAuthors.xml"));

         final Feed f = result.getFeed();
         checkAuthors(f.authors());
      }

      @Test
      public void shouldReadFeedContributors() throws Exception {
         final ParserResult result = getParser().read(openFeedResource("FeedWithContributors.xml"));

         final Feed f = result.getFeed();
         assertEquals(3, f.contributors().size());
         checkContributors(f.contributors());
      }
   }

   public static class WhenParsingEntryElements extends TestParent {

      @Test
      public void shouldReturnEmptyEntry() throws Exception {
         final String feedXml = "<entry />";
         final ParserResult result = getParser().read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getEntry());
      }

      @Test
      public void shouldReturnFeedWithEmptyEntry() throws Exception {
         final String feedXml = "<feed><entry /></feed>";
         final ParserResult result = getParser().read(new ByteArrayInputStream(feedXml.getBytes()));

         final Feed f = result.getFeed();

         assertNotNull(f);
         assertEquals(1, f.entries().size());
      }

      @Test
      public void shouldReadEntryAttributes() throws Exception {
         final String feedXml = "<feed><entry lang='en' base='uri' /></feed>";
         final ParserResult result = getParser().read(new ByteArrayInputStream(feedXml.getBytes()));

         final Feed f = result.getFeed();

         assertNotNull(result.getFeed());
         assertEquals(1, result.getFeed().entries().size());

         final Entry e = f.entries().get(0);

         assertEquals("en", e.lang());
         assertEquals(URI.create("uri"), e.base());
      }

      @Test
      public void shouldReadEntryAuthors() throws Exception {
         final ParserResult result = getParser().read(openEntryResource("EntryWithAuthors.xml"));

         final Entry e = result.getEntry();
         checkAuthors(e.authors());
      }

      @Test
      public void shouldReadEntryContributors() throws Exception {
         final ParserResult result = getParser().read(openEntryResource("EntryWithContributors.xml"));

         final Entry e = result.getEntry();
         assertEquals(3, e.contributors().size());
         checkContributors(e.contributors());
      }

      @Test
      public void shouldReadEntryContent() throws Exception {
         final ParserResult result = getParser().read(openEntryResource("EntryWithContent.xml"));

         final Entry e = result.getEntry();
         assertEquals("Text content.", e.content().value());
      }

      @Test
      public void shouldReadEntryCategories() throws Exception {
         final ParserResult result = getParser().read(openEntryResource("EntryWithCategories.xml"));

         final Entry e = result.getEntry();
         checkCategories(e.categories());
      }
   }
}
