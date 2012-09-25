package org.atomnuke.kernel.shutdown;

import org.atomnuke.kernel.resource.Destroyable;

/**
 *
 * @author zinic
 */
public interface ShutdownHook {

   void enlist(Destroyable destroyable);

   void shutdown();
}
