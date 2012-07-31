package net.jps.nuke.examples.listener;

import net.jps.nuke.service.DestructionException;
import net.jps.nuke.service.InitializationException;
import net.jps.nuke.source.AtomSource;
import net.jps.nuke.source.AtomSourceException;
import net.jps.nuke.source.AtomSourceResult;

/**
 *
 * @author zinic
 */
public class ClasspathSource implements AtomSource {

   @Override
   public void init() throws InitializationException {
   }

   @Override
   public void destroy() throws DestructionException {
   }

   @Override
   public AtomSourceResult poll() throws AtomSourceException {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
