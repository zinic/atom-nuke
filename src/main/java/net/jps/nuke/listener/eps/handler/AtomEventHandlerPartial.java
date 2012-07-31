package net.jps.nuke.listener.eps.handler;

import net.jps.nuke.service.DestructionException;
import net.jps.nuke.service.InitializationException;

/**
 *
 * @author zinic
 */
public abstract class AtomEventHandlerPartial implements AtomEventlet {

   @Override
   public void init() throws InitializationException {
   }

   @Override
   public void destroy() throws DestructionException {
   }
}
