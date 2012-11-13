package org.atomnuke.util.lifecycle.runnable;

import org.atomnuke.lifecycle.Reclaimable;
import org.atomnuke.task.TaskHandle;

/**
 *
 * @author zinic
 */
public interface ReclaimableTask extends Reclaimable, Runnable {

   void enlisted(TaskHandle taskHandle);
}
