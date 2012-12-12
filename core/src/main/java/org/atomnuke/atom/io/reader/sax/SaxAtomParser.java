package org.atomnuke.atom.io.reader.sax;

import com.rackspace.papi.commons.util.pooling.Pool;
import com.rackspace.papi.commons.util.pooling.ResourceContext;
import com.rackspace.papi.commons.util.pooling.ResourceContextException;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import org.atomnuke.atom.io.AtomReadException;
import org.atomnuke.atom.io.AtomReader;
import org.atomnuke.atom.io.ReaderResult;

/**
 *
 * @author zinic
 */
public class SaxAtomParser implements AtomReader {

   private final Pool<SAXParser> parserPool;

   public SaxAtomParser(Pool<SAXParser> parserPool) {
      this.parserPool = parserPool;
   }

   @Override
   public ReaderResult read(final InputStream source) throws AtomReadException {
      try {
         return parserPool.use(new ResourceContext<SAXParser, ReaderResult>() {
            @Override
            public ReaderResult perform(SAXParser parser) {
               try {
                  final AtomHandler handler = new AtomHandler(parser.getXMLReader());
                  parser.parse(source, handler);

                  return handler.getResult();
               } catch (Exception ex) {
                  throw new ResourceContextException(ex.getMessage(), ex);
               }
            }
         });
      } catch (ResourceContextException e) {
         throw new AtomReadException(e.getMessage(), e.getCause());
      }
   }
}