package org.atomnuke.examples;

import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.atomnuke.atom.io.AtomReaderFactory;
import org.atomnuke.atom.io.AtomWriterFactory;
import org.atomnuke.atom.io.reader.sax.SaxAtomReaderFactory;
import org.atomnuke.atom.io.writer.stax.StaxAtomWriterFactory;

/**
 *
 * @author zinic
 */
public class PerformanceMain {

   public static InputStream open(String name) throws IOException {
      return PerformanceMain.class.getResourceAsStream("/META-INF/examples/atom/" + name);
   }

   public static void main(String[] args) throws Exception {
      int iterationLimit = 20000;
      long tstampMillis = System.currentTimeMillis();
      int elapsed;

      final AtomWriterFactory writerFactory = new StaxAtomWriterFactory();
      final AtomReaderFactory readerFactory = new SaxAtomReaderFactory();
      final byte[] bytes = RawInputStreamReader.instance().readFully(open("PerformanceTestContents.xml"));
      final ByteArrayOutputStream output = new ByteArrayOutputStream();

      for (int i = 0; i < iterationLimit; i++) {
         writerFactory.getInstance().write(output, readerFactory.getInstance().read(new ByteArrayInputStream(bytes)).getFeed());
         output.reset();
      }

      elapsed = (int) (System.currentTimeMillis() - tstampMillis);

      System.out.println("Elapsed time: " + elapsed + "ms - Average runtime: " + ((double) elapsed / (double) iterationLimit) + "ms");
   }
}
