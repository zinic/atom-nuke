package net.jps.nuke.atom.sax;

import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.jps.nuke.atom.AtomParserException;
import net.jps.nuke.atom.FeedParser;
import net.jps.nuke.atom.ParserResult;

/**
 *
 * @author zinic
 */
public class SaxAtomParser implements FeedParser {

   private final SAXParserFactory parserFactory;

   public SaxAtomParser() {
      this(SAXParserFactory.newInstance());
   }

   public SaxAtomParser(SAXParserFactory parserFactory) {
      this.parserFactory = parserFactory;
   }

   @Override
   public ParserResult read(InputStream source) throws AtomParserException {
      final AtomHandler handler = new AtomHandler();

      try {
         final SAXParser parser = parserFactory.newSAXParser();
         parser.parse(source, handler);
      } catch (Exception e) {
         throw new AtomParserException(e.getMessage(), e.getCause());
      }

      return handler.getResult();
   }
}