package net.jps.nuke.atom.stax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.Writer;
import net.jps.nuke.atom.model.Author;
import net.jps.nuke.atom.model.Contributor;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.atom.model.Link;
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
                  
                  assertNotNull(rereadAuthor.base());
                  assertNotNull(rereadAuthor.lang());
                  assertNotNull(rereadAuthor.email());
                  assertNotNull(rereadAuthor.uri());

                  assertEquals(originalAuthor.email(), rereadAuthor.email());
                  assertEquals(originalAuthor.uri(), rereadAuthor.uri());
                  assertEquals(originalAuthor.lang(), rereadAuthor.lang());
                  assertEquals(originalAuthor.base(), rereadAuthor.base());
                  break;
               }
            }
         }

         assertTrue("All authors must match original set.", rereadAuthors.isEmpty());
      }

      @Test
      public void shouldReadLinks() throws Exception {
         final List<Link> originalLinks = originalFeed.links(), rereadLinks = writtenFeed.links();

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
      
      @Test
      public void shouldReadContributors() throws Exception {
         final List<Contributor> originalContributors = originalFeed.contributors(), rereadContributors = writtenFeed.contributors();

         assertEquals(originalContributors.size(), rereadContributors.size());

         for (Contributor originalContributor : originalContributors) {
            for (Iterator<Contributor> contributorItr = rereadContributors.iterator(); contributorItr.hasNext();) {
               final Contributor rereadContributor = contributorItr.next();

               if (originalContributor.name().equals(rereadContributor.name())) {
                  contributorItr.remove();

                  assertNotNull(rereadContributor.base());
                  assertNotNull(rereadContributor.lang());
                  assertNotNull(rereadContributor.email());
                  assertNotNull(rereadContributor.uri());

                  assertEquals(originalContributor.email(), rereadContributor.email());
                  assertEquals(originalContributor.uri(), rereadContributor.uri());
                  assertEquals(originalContributor.lang(), rereadContributor.lang());
                  assertEquals(originalContributor.base(), rereadContributor.base());
                  break;
               }
            }
         }

         assertTrue("All contributors must match original set.", rereadContributors.isEmpty());
      }
   }
}
