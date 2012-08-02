package net.jps.nuke.listener.eps;

import net.jps.nuke.listener.eps.eventlet.AtomEventletException;
import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.listener.eps.eventlet.AtomEventlet;
import net.jps.nuke.listener.eps.selector.Selector;
import net.jps.nuke.listener.eps.selector.SelectorResult;
import net.jps.nuke.service.DestructionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class HandlerConduit {

   private static final Logger LOG = LoggerFactory.getLogger(HandlerConduit.class);
   
   private final AtomEventlet eventHandler;
   private final Selector selector;

   public HandlerConduit(AtomEventlet eventHandler, Selector selector) {
      this.eventHandler = eventHandler;
      this.selector = selector;
   }
   
   public void destroy() throws DestructionException {
      eventHandler.destroy();
   }

   public SelectorResult select(Feed page) {
      final SelectorResult result = selector.select(page);

      if (result == SelectorResult.PROCESS) {
         for (Entry entry : page.entries()) {
            select(entry);
         }
      }

      return result;
   }

   public SelectorResult select(Entry entry) {
      final SelectorResult result = selector.select(entry);

      if (result == SelectorResult.PROCESS) {
         try {
            eventHandler.entry(entry);
         } catch (AtomEventletException epe) {
            LOG.error(epe.getMessage(), epe);
         }
      }

      return result;
   }
}
