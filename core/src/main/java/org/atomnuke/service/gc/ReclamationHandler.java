package org.atomnuke.service.gc;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.lifecycle.Reclaimable;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface ReclamationHandler extends Reclaimable {

   void garbageCollect();

   CancellationRemote watch(Reclaimable reclaimableInstance);

   CancellationRemote watch(InstanceContext<? extends Reclaimable> reclaimableInstance);
}
