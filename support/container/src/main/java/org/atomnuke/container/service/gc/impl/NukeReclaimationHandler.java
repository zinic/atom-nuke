package org.atomnuke.container.service.gc.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.container.service.gc.ReclaimationHandler;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.lifecycle.Reclaimable;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class NukeReclaimationHandler implements ReclaimationHandler {

   private final List<ReclaimationHandle> reclaimationList;

   public NukeReclaimationHandler() {
      reclaimationList = new LinkedList<ReclaimationHandle>();
   }

   @Override
   public void destroy() {
      for (ReclaimationHandle reclaimationHandle : reclaimationList) {
         reclaimationHandle.reclaim();
      }

      reclaimationList.clear();
   }

   @Override
   public synchronized void garbageCollect() {
      for (Iterator<ReclaimationHandle> handleItr = reclaimationList.iterator(); handleItr.hasNext();) {
         final ReclaimationHandle handle = handleItr.next();

         if (handle.shouldReclaim()) {
            handle.reclaim();
            handleItr.remove();
         }
      }
   }

   @Override
   public synchronized CancellationRemote watch(InstanceContext<Reclaimable> reclaimableInstance) {
      final CancellationRemote cancellationRemote = new AtomicCancellationRemote();

      reclaimationList.add(new ReclaimationHandle(reclaimableInstance, cancellationRemote));

      return cancellationRemote;
   }
}
