package org.atomnuke.atom.sax.impl;

import com.rackspace.papi.commons.util.pooling.ConstructionStrategy;
import com.rackspace.papi.commons.util.pooling.GenericBlockingResourcePool;
import com.rackspace.papi.commons.util.pooling.Pool;
import com.rackspace.papi.commons.util.pooling.ResourceConstructionException;
import com.rackspace.papi.commons.util.pooling.ResourceContext;
import com.rackspace.papi.commons.util.pooling.ResourceContextException;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.atomnuke.atom.AtomParserException;
import org.atomnuke.atom.ParserResult;
import org.atomnuke.atom.Reader;

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
      parserFactory.setNamespaceAware(true);
      
      this.parserPool = new GenericBlockingResourcePool<SAXParser>(new ConstructionStrategy<SAXParser>() {
         @Override
         public SAXParser construct() {
            try {
               return parserFactory.newSAXParser();
            } catch (Exception ex) {
               throw new ResourceConstructionException(ex.getMessage(), ex);
            }
         }
      });
   }

   @Override
   public ParserResult read(final InputStream source) throws AtomParserException {
      try {
         return parserPool.use(new ResourceContext<SAXParser, ParserResult>() {
            @Override
            public ParserResult perform(SAXParser parser) {
               try {
                  final AtomHandler handler = new AtomHandler(parser.getXMLReader());
                  parser.parse(source, handler);

                  return handler.getResult();
               } catch (Exception ex) {
                  throw new ResourceContextException(ex.getMessage(), ex);
               }
            }
         });
      } catch (Exception e) {
         throw new AtomParserException(e.getMessage(), e.getCause());
      }
   }
}