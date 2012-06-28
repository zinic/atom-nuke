package net.jps.nuke.atom.sax;

import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.ByteArrayInputStream;
import java.net.URI;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 *
 * @author zinic
 */
@RunWith(Enclosed.class)
public class AtomHandlerTest {

   public static class WhenParsingFeedElements {

      @Test
      public void shouldReturnEmptyFeed() throws Exception {
         final String feedXml = "<feed />";

         final SaxAtomParser parser = new SaxAtomParser();
         final ParserResult result = parser.read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getFeed());
      }

      @Test
      public void shouldReadFeedAttributes() throws Exception {
         final String feedXml = "<feed lang='en' base='uri'></feed>";

         final SaxAtomParser parser = new SaxAtomParser();
         final ParserResult result = parser.read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getFeed());
         assertEquals("en", result.getFeed().lang());
         assertEquals(URI.create("uri"), result.getFeed().base());
      }
   }

   public static class WhenParsingEntryElements {

      @Test
      public void shouldReturnEmptyEntry() throws Exception {
         final String feedXml = "<entry />";

         final SaxAtomParser parser = new SaxAtomParser();
         final ParserResult result = parser.read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getEntry());
      }

      @Test
      public void shouldReturnFeedWithEmptyEntry() throws Exception {
         final String feedXml = "<feed><entry /></feed>";

         final SaxAtomParser parser = new SaxAtomParser();
         final ParserResult result = parser.read(new ByteArrayInputStream(feedXml.getBytes()));

         final Feed f = result.getFeed();
         
         assertNotNull(f);
         assertEquals(1, f.entries().size());
      }

      @Test
      public void shouldReadEntryAttributes() throws Exception {
         final String feedXml = "<feed><entry lang='en' base='uri' /></feed>";

         final SaxAtomParser parser = new SaxAtomParser();
         final ParserResult result = parser.read(new ByteArrayInputStream(feedXml.getBytes()));

         final Feed f = result.getFeed();
         
         assertNotNull(result.getFeed());
         assertEquals(1, result.getFeed().entries().size());
         
         final Entry e = f.entries().get(0);
         
         assertEquals("en", e.lang());
         assertEquals(URI.create("uri"), e.base());
      }
      
      @Test
      public void shouldReadEntryAuthors() throws Exception {
         final byte[] feedXml = RawInputStreamReader.instance().readFully(AtomHandlerTest.class.getResourceAsStream("/META-INF/examples/atom/EntryWithAuthors.xml"));

         final SaxAtomParser parser = new SaxAtomParser();
         final ParserResult result = parser.read(new ByteArrayInputStream(feedXml));

         final Entry e = result.getEntry();
         assertEquals(3, e.authors().size());
      }
   }
}
