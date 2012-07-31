package net.jps.nuke.atom.stax;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.Writer;
import net.jps.nuke.atom.model.builder.FeedBuilder;
import net.jps.nuke.atom.sax.impl.SaxAtomParser;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 *
 * @author zinic
 */
@RunWith(Enclosed.class)
public class AtomWriterTest {

   public static class WhenWritingFeeds {

      @Test
      public void shouldWriteEmptyFeed() throws Exception {
         final Reader reader = new SaxAtomParser();
         final ParserResult result = reader.read(AtomWriterTest.class.getResourceAsStream("/META-INF/examples/atom/PerformanceTestContents.xml"));

         final Writer writer = new StaxAtomWriter();
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();

         writer.write(baos, result.getFeed());

         final ParserResult rereadResult = reader.read(new ByteArrayInputStream(baos.toByteArray()));

         assertEquals(result.getFeed().authors().size(), rereadResult.getFeed().authors().size());
         assertEquals(result.getFeed().categories().size(), rereadResult.getFeed().categories().size());
         assertEquals(result.getFeed().links().size(), rereadResult.getFeed().links().size());
         assertEquals(result.getFeed().entries().size(), rereadResult.getFeed().entries().size());
      }
   }
}
