package org.atomnuke.performance;

import com.rackspace.papi.commons.util.io.RawInputStreamReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.atomnuke.atom.sax.impl.SaxAtomParser;
import org.atomnuke.atom.stax.StaxAtomWriter;

/**
 *
 * @author zinic
 */
public class ParserPerformance {

   public static InputStream open(String name) throws IOException {
      return ParserPerformance.class.getResourceAsStream("/META-INF/examples/atom/" + name);
   }

   public static void main(String[] args) throws Exception {
      int iterationLimit = 20000;
      long tstampMillis = System.currentTimeMillis();
      int elapsed;

      final StaxAtomWriter writer = new StaxAtomWriter();
      final SaxAtomParser parser = new SaxAtomParser();
      final byte[] bytes = RawInputStreamReader.instance().readFully(open("PerformanceTestContents.xml"));
      final ByteArrayOutputStream output = new ByteArrayOutputStream();

      for (int i = 0; i < iterationLimit; i++) {
         parser.read(new ByteArrayInputStream(bytes)).getFeed();
         writer.write(output, parser.read(new ByteArrayInputStream(bytes)).getFeed());
         output.reset();
      }

      elapsed = (int) (System.currentTimeMillis() - tstampMillis);

      System.out.println("Elapsed time: " + elapsed + "ms - Average runtime: " + ((double) elapsed / (double) iterationLimit) + "ms");
   }
}
