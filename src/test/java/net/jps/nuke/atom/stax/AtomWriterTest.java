package net.jps.nuke.atom.stax;

import java.io.ByteArrayOutputStream;
import net.jps.nuke.atom.Writer;
import net.jps.nuke.atom.model.builder.FeedBuilder;
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
         final Writer writer = new StaxAtomWriter();
         final ByteArrayOutputStream baos = new ByteArrayOutputStream();
         
         writer.write(baos, FeedBuilder.newBuilder().build());
         assertEquals("<?xml version='1.0' encoding='UTF-8'?><feed xmlns=\"http://www.w3.org/2005/Atom\"/>", new String(baos.toByteArray()));
      }
   }
}
