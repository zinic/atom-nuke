package org.atomnuke.service.gc.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.plugin.env.NopInstanceEnvironment;
import org.atomnuke.lifecycle.Reclaimable;
import org.atomnuke.util.remote.AtomicCancellationRemote;
import org.atomnuke.util.remote.CancellationRemote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class NukeReclamationHandler implements ReclamationHandler {

   private static final Logger LOG = LoggerFactory.getLogger(NukeReclamationHandler.class);

   private final List<ReclamationHandle> reclamationList;

   public NukeReclamationHandler() {
      reclamationList = new LinkedList<ReclamationHandle>();
   }

   @Override
   public void destroy() {
      LOG.info("Removing " + reclamationList.size() + " objets marked for reclamation.");

      for (ReclamationHandle reclamationHandle : reclamationList) {
         reclamationHandle.reclaim();
      }

      reclamationList.clear();
   }

   @Override
   public synchronized void garbageCollect() {
      for (Iterator<ReclamationHandle> handleItr = reclamationList.iterator(); handleItr.hasNext();) {
         final ReclamationHandle handle = handleItr.next();

         if (handle.shouldReclaim()) {
            handle.reclaim();
            handleItr.remove();
         }
      }
   }

   @Override
   public CancellationRemote watch(Reclaimable reclaimable) {
      return watch(new InstanceContextImpl<Reclaimable>(NopInstanceEnvironment.getInstance(), reclaimable));
   }

   @Override
   public synchronized CancellationRemote watch(InstanceContext<? extends Reclaimable> reclaimableInstance) {
      final CancellationRemote cancellationRemote = new AtomicCancellationRemote();

      reclamationList.add(new ReclamationHandle((InstanceContext<Reclaimable>) reclaimableInstance, cancellationRemote));

      return cancellationRemote;
   }
}
