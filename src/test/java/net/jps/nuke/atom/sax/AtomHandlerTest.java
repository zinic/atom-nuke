package net.jps.nuke.atom.sax;

import java.io.ByteArrayInputStream;
import java.net.URI;
import net.jps.nuke.atom.ParserResult;
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
         final String feedXml = "<feed></feed>";

         final SaxAtomParser parser = new SaxAtomParser();
         final ParserResult result = parser.read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getFeed());
      }

      @Test
      public void shouldReadFeedAttributes() throws Exception {
         final String feedXml = "<feed lang='en' base='./feed.xml'></feed>";

         final SaxAtomParser parser = new SaxAtomParser();
         final ParserResult result = parser.read(new ByteArrayInputStream(feedXml.getBytes()));

         assertNotNull(result.getFeed());
         assertEquals("en", result.getFeed().lang());
         assertEquals(URI.create("./feed.xml"), result.getFeed().base());
      }
   }
}
