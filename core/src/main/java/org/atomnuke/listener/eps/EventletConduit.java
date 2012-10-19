package org.atomnuke.listener.eps;

import org.atomnuke.listener.eps.eventlet.AtomEventletException;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.listener.eps.selector.Selector;
import org.atomnuke.listener.eps.selector.SelectorResult;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class EventletConduit {

   private static final ComplexOperation<AtomEventlet, Entry> ENTRY_OPERATION = new ComplexOperation<AtomEventlet, Entry>() {
      @Override
      public void perform(AtomEventlet instance, Entry argument) throws OperationFailureException {
         try {
            instance.entry(argument);
         } catch (AtomEventletException aee) {
            throw new OperationFailureException(aee);
         }
      }
   };

   private final InstanceContext<AtomEventlet> eventletContext;
   private final CancellationRemote cancellationRemote;
   private final Selector selector;

   public EventletConduit(InstanceContext<AtomEventlet> eventletContext, Selector selector) {
      this.eventletContext = eventletContext;
      this.selector = selector;

      cancellationRemote = new AtomicCancellationRemote();
   }

   public void destroy() {
      eventletContext.perform(ReclaimOperation.<AtomEventlet>instance());
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
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
         eventletContext.perform(ENTRY_OPERATION, entry);
      }

      return result;
   }
}
