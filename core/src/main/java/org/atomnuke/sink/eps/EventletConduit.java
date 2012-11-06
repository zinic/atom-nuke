package org.atomnuke.sink.eps;

import org.atomnuke.sink.eps.eventlet.AtomEventletException;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.sink.eps.eventlet.AtomEventlet;
import org.atomnuke.sink.eps.selector.EntrySelector;
import org.atomnuke.sink.eps.selector.SelectorResult;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class EventletConduit {

   private static final Logger LOG = LoggerFactory.getLogger(EventletConduit.class);
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
   private final EntrySelector selector;

   public EventletConduit(InstanceContext<AtomEventlet> eventletContext, CancellationRemote cancellationRemote, EntrySelector selector) {
      this.eventletContext = eventletContext;
      this.cancellationRemote = cancellationRemote;
      this.selector = selector;
   }

   public void destroy() {
      try {
         eventletContext.perform(ReclaimOperation.<AtomEventlet>instance());
      } catch (OperationFailureException ofe) {
         LOG.error("Failed to destroy conduit: " + this + " - Reason: " + ofe.getMessage(), ofe);
      }
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public SelectorResult select(Entry entry) {
      return selector.select(entry);
   }

   public void perform(Entry entry) {
      try {
         eventletContext.perform(ENTRY_OPERATION, entry);
      } catch (OperationFailureException ofe) {
         LOG.error("Failed to perform entry operation - Reason: " + ofe.getMessage(), ofe);
      }
   }
}
