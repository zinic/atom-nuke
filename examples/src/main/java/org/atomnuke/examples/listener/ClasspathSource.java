package org.atomnuke.examples.listener;

import org.atomnuke.atom.AtomParserException;
import org.atomnuke.atom.ParserResult;
import org.atomnuke.atom.Reader;
import org.atomnuke.atom.sax.impl.SaxAtomParser;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.AtomSourceResult;
import org.atomnuke.source.impl.AtomSourceResultImpl;
import org.atomnuke.task.context.TaskContext;

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
   public void init(TaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy(TaskContext tc) throws DestructionException {
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
