package net.jps.nuke.examples.listener;

import net.jps.nuke.atom.AtomParserException;
import net.jps.nuke.atom.ParserResult;
import net.jps.nuke.atom.Reader;
import net.jps.nuke.atom.sax.impl.SaxAtomParser;
import net.jps.nuke.service.DestructionException;
import net.jps.nuke.service.InitializationException;
import net.jps.nuke.source.AtomSource;
import net.jps.nuke.source.AtomSourceException;
import net.jps.nuke.source.AtomSourceResult;
import net.jps.nuke.source.impl.AtomSourceResultImpl;

/**
 *
 * @author zinic
 */
public class ClasspathSource implements AtomSource {

   private final String resourcePath;
   private final Reader reader;

   public ClasspathSource(String resourcePath) {
      this.resourcePath = resourcePath;

      reader = new SaxAtomParser();
   }

   @Override
   public void init() throws InitializationException {
   }

   @Override
   public void destroy() throws DestructionException {
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      try {
         final ParserResult parserResult = reader.read(ClasspathSource.class.getResourceAsStream(resourcePath));

         if (parserResult.getFeed() != null) {
            return new AtomSourceResultImpl(parserResult.getFeed());
         } else {
            return new AtomSourceResultImpl(parserResult.getEntry());
         }
      } catch (AtomParserException ape) {
         throw new AtomSourceException(ape);
      }
   }
}