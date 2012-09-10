package org.atomnuke.atom.io.reader.sax;

import com.rackspace.papi.commons.util.pooling.ConstructionStrategy;
import com.rackspace.papi.commons.util.pooling.GenericBlockingResourcePool;
import com.rackspace.papi.commons.util.pooling.Pool;
import com.rackspace.papi.commons.util.pooling.ResourceConstructionException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.atomnuke.atom.io.AtomReader;
import org.atomnuke.atom.io.AtomReaderFactory;

/**
 *
 * @author zinic
 */
public final class SaxAtomReaderFactory implements AtomReaderFactory {

   private final SAXParserFactory parserFactory;
   private final Pool<SAXParser> parserPool;

   public SaxAtomReaderFactory() {
      this(SAXParserFactory.newInstance());
   }

   public SaxAtomReaderFactory(SAXParserFactory parserFactoryInst) {
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
      }, 2, 32);
   }

   @Override
   public final AtomReader getInstance() {
      return new SaxAtomParser(parserPool);
   }
}
