package net.jps.nuke.atom.sax.impl;

import com.rackspace.papi.commons.util.pooling.ConstructionStrategy;
import com.rackspace.papi.commons.util.pooling.GenericBlockingResourcePool;
import com.rackspace.papi.commons.util.pooling.Pool;
import com.rackspace.papi.commons.util.pooling.ResourceConstructionException;
import com.rackspace.papi.commons.util.pooling.ResourceContext;
import com.rackspace.papi.commons.util.pooling.ResourceContextException;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.jps.nuke.atom.AtomParserException;
import net.jps.nuke.atom.Result;
import net.jps.nuke.atom.Reader;

/**
 *
 * @author zinic
 */
public class SaxAtomParser implements Reader {

   private final SAXParserFactory parserFactory;
   private final Pool<SAXParser> parserPool;

   public SaxAtomParser() {
      this(SAXParserFactory.newInstance());
   }

   public SaxAtomParser(SAXParserFactory parserFactoryInst) {
      this.parserFactory = parserFactoryInst;
      this.parserPool = new GenericBlockingResourcePool<SAXParser>(new ConstructionStrategy<SAXParser>() {
         public SAXParser construct() throws ResourceConstructionException {
            try {
               return parserFactory.newSAXParser();
            } catch (Exception ex) {
               throw new ResourceConstructionException(ex.getMessage(), ex);
            }
         }
      });
   }

   public Result read(final InputStream source) throws AtomParserException {
      try {
         return parserPool.use(new ResourceContext<SAXParser, Result>() {
            public Result perform(SAXParser parser) throws ResourceContextException {
               try {
                  final AtomHandler handler = new AtomHandler(parser.getXMLReader());
                  parser.parse(source, handler);

                  return handler.getResult();
               } catch (Exception ex) {
                  ex.printStackTrace();
                  throw new ResourceContextException(ex.getMessage(), ex);
               }
            }
         });
      } catch (Exception e) {
         e.printStackTrace();
         throw new AtomParserException(e.getMessage(), e.getCause());
      }
   }
}