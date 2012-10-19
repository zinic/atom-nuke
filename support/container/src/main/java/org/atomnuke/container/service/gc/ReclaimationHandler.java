package org.atomnuke.container.service.gc;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.lifecycle.Reclaimable;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface ReclaimationHandler extends Reclaimable {

   void garbageCollect();

   CancellationRemote watch(InstanceContext<Reclaimable> reclaimableInstance);
}
