package org.atomnuke.sink.manager;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class SinkManagerImpl implements SinkManager {

   private final ReclamationHandler reclamationHandler;
   private final List<ManagedSink> sinks;

   public SinkManagerImpl(ReclamationHandler reclamationHandler) {
      this.reclamationHandler = reclamationHandler;

      sinks = new LinkedList<ManagedSink>();
   }

   @Override
   public synchronized boolean hasRegisteredSinks() {
      return !sinks.isEmpty();
   }

   @Override
   public synchronized List<ManagedSink> sinks() {
      for (Iterator<ManagedSink> registeredSinkItr = sinks.iterator(); registeredSinkItr.hasNext();) {
         final ManagedSink managedSink = registeredSinkItr.next();

         if (managedSink.cancellationRemote().canceled()) {
            registeredSinkItr.remove();
         }
      }

      return Collections.unmodifiableList(sinks);
   }

   @Override
   public synchronized CancellationRemote addSink(InstanceContext<? extends AtomSink> atomSinkContext) {
      final CancellationRemote cancellationRemote = reclamationHandler.watch(atomSinkContext);
      final UUID taskId = UUID.randomUUID();

      final ManagedSink newSink = new ManagedSink(atomSinkContext, cancellationRemote, taskId);
      sinks.add(newSink);

      return cancellationRemote;
   }
}
