package org.atomnuke.examples.sinks;

import java.io.InputStream;
import org.atomnuke.atom.io.AtomReaderFactory;
import org.atomnuke.atom.io.ReaderResult;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.source.result.AtomSourceResultImpl;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.lifecycle.DestructionException;
import org.atomnuke.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public class ClasspathSource implements AtomSource {

   private final AtomReaderFactory atomReaderFactory;
   private final String resourcePath;

   public ClasspathSource(AtomReaderFactory atomReaderFactory, String resourcePath) {
      this.atomReaderFactory = atomReaderFactory;
      this.resourcePath = resourcePath;
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      try {
         final InputStream in = ClasspathSource.class.getResourceAsStream(resourcePath);
         final ReaderResult result = atomReaderFactory.getInstance().read(in);

         if (result.isEntry()) {
            return new AtomSourceResultImpl(result.getEntry());
         } else {
            return new AtomSourceResultImpl(result.getFeed());
         }
      } catch (Exception ex) {
         throw new AtomSourceException(ex.getMessage(), ex);
      }
   }

   @Override
   public void init(AtomTaskContext tc) throws InitializationException {
   }

   @Override
   public void destroy() {
   }
}
