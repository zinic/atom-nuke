package net.jps.nuke.listener.eps;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.atom.model.Feed;
import net.jps.nuke.listener.eps.handler.AtomEventHandler;
import net.jps.nuke.listener.eps.handler.Selector;
import net.jps.nuke.listener.eps.handler.SelectorResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class Conduit {

   private static final Logger LOG = LoggerFactory.getLogger(Conduit.class);
   private final AtomEventHandler eventHandler;
   private final Selector selector;

   public Conduit(AtomEventHandler eventHandler, Selector selector) {
      this.eventHandler = eventHandler;
      this.selector = selector;
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
         } catch (EventProcessingException epe) {
            LOG.error(epe.getMessage(), epe);
         }
      }

      return result;
   }
}
