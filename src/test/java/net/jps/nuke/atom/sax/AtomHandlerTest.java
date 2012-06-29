package net.jps.nuke.atom.sax;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.model.Author;
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
      return  AtomHandlerTest.class.getResourceAsStream("/META-INF/examples/atom/entry/" + name);
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
         final List<Author> authors = f.authors();

         assertEquals("John Doe", authors.get(0).name());
         assertEquals("http://john.doe.example.domain/", authors.get(0).uri());
         assertEquals("john.doe@example.domain", authors.get(0).email());

         assertEquals("John Doe", authors.get(1).name());
         assertEquals("http://john.doe.example.domain/", authors.get(1).uri());
         assertEquals("john.doe@example.domain", authors.get(1).email());

         assertEquals("John Doe", authors.get(2).name());
         assertEquals("http://john.doe.example.domain/", authors.get(2).uri());
         assertEquals("john.doe@example.domain", authors.get(2).email());
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
         assertEquals(3, e.authors().size());

         final List<Author> authors = e.authors();

         assertEquals("John Doe", authors.get(0).name());
         assertEquals("http://john.doe.example.domain/", authors.get(0).uri());
         assertEquals("john.doe@example.domain", authors.get(0).email());

         assertEquals("John Doe", authors.get(1).name());
         assertEquals("http://john.doe.example.domain/", authors.get(1).uri());
         assertEquals("john.doe@example.domain", authors.get(1).email());

         assertEquals("John Doe", authors.get(2).name());
         assertEquals("http://john.doe.example.domain/", authors.get(2).uri());
         assertEquals("john.doe@example.domain", authors.get(2).email());
      }

      @Test @Ignore
      public void shouldReadEntryContributors() throws Exception {
         final ParserResult result = getParser().read(openEntryResource("EntryWithContributors.xml"));

         final Entry e = result.getEntry();
         assertEquals(3, e.authors().size());

         final List<Contributor> contributors = e.contributors();

         assertEquals("John Doe", contributors.get(0).name());
         assertEquals("http://john.doe.example.domain/", contributors.get(0).uri());
         assertEquals("john.doe@example.domain", contributors.get(0).email());

         assertEquals("John Doe", contributors.get(1).name());
         assertEquals("http://john.doe.example.domain/", contributors.get(1).uri());
         assertEquals("john.doe@example.domain", contributors.get(1).email());

         assertEquals("John Doe", contributors.get(2).name());
         assertEquals("http://john.doe.example.domain/", contributors.get(2).uri());
         assertEquals("john.doe@example.domain", contributors.get(2).email());
      }
   }
}
