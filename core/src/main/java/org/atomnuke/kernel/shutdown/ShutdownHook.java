package org.atomnuke.kernel.shutdown;

import org.atomnuke.util.lifecycle.Reclaimable;

/**
 *
 * @author zinic
 */
public interface ShutdownHook {

   void enlist(Reclaimable destroyable);

   void shutdown();
}
