package org.atomnuke.service.gc.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.service.gc.ReclaimationHandler;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.lifecycle.Reclaimable;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class NukeReclaimationHandler implements ReclaimationHandler {

   private static final Logger LOG = LoggerFactory.getLogger(NukeReclaimationHandler.class);

   private final List<ReclaimationHandle> reclaimationList;

   public NukeReclaimationHandler() {
      reclaimationList = new LinkedList<ReclaimationHandle>();
   }

   @Override
   public void destroy() {
      LOG.info("Removing " + reclaimationList.size() + " objets marked for reclaimation.");

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
   public synchronized CancellationRemote watch(InstanceContext<? extends Reclaimable> reclaimableInstance) {
      final CancellationRemote cancellationRemote = new AtomicCancellationRemote();

      reclaimationList.add(new ReclaimationHandle((InstanceContext<Reclaimable>) reclaimableInstance, cancellationRemote));

      return cancellationRemote;
   }
}
