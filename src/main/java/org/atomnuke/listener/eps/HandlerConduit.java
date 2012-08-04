package org.atomnuke.listener.eps;

import org.atomnuke.listener.eps.eventlet.AtomEventletException;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.listener.eps.selector.Selector;
import org.atomnuke.listener.eps.selector.SelectorResult;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.context.TaskContext;
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
   
   public void destroy(TaskContext tc) throws DestructionException {
      eventHandler.destroy(tc);
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
