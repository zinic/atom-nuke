package org.atomnuke.task.impl;

import org.atomnuke.task.TaskHandle;

/**
 *
 * @author zinic
 */
public class ManagedRunTask extends AbstractManagedTask {

   private final Runnable runnable;

   public ManagedRunTask(Runnable runnable, TaskHandle taskHandle) {
      super(taskHandle);

      this.runnable = runnable;
   }

   @Override
   public void run() {
      runnable.run();
   }
}
