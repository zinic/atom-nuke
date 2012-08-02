package net.jps.nuke.atom.stax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.Writer;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Feed;
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

      protected Feed originalFeed, writtenFeed;

      @Before
      public void beforeAny() throws Exception {
         final Reader reader = new SaxAtomParser();
         final ParserResult originalResult = reader.read(AtomWriterTest.class.getResourceAsStream("/META-INF/examples/atom/PerformanceTestContents.xml"));

         originalFeed = originalResult.getFeed();

         final Writer writer = new StaxAtomWriter();
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();

         writer.write(baos, originalFeed);

         final ParserResult rereadResult = reader.read(new ByteArrayInputStream(baos.toByteArray()));
         writtenFeed = rereadResult.getFeed();
      }
   }

   public static class WhenWritingFeeds extends TestParent {

      @Test
      public void shouldReadAuthors() throws Exception {
         final List<Author> originalAuthors = originalFeed.authors(), rereadAuthors = writtenFeed.authors();

         assertEquals(originalAuthors.size(), rereadAuthors.size());
         
         for (Author originalAuthor : originalAuthors) {
            for (Iterator<Author> authorItr = rereadAuthors.iterator(); authorItr.hasNext();) {
               final Author rereadAuthor = authorItr.next();
               
               if (originalAuthor.name().equals(rereadAuthor.name())) {
                  authorItr.remove();
                  
                  assertEquals(originalAuthor.email(), rereadAuthor.email());
                  assertEquals(originalAuthor.uri(), rereadAuthor.uri());
                  break;
               }
            }
         }
         
         assertTrue("All authors must match original set.", rereadAuthors.isEmpty());
      }
   }
//         assertEquals(original.getFeed().categories().size(), rereadResult.getFeed().categories().size());
//         assertEquals(original.getFeed().links().size(), rereadResult.getFeed().links().size());
//         assertEquals(original.getFeed().entries().size(), rereadResult.getFeed().entries().size());   
}
