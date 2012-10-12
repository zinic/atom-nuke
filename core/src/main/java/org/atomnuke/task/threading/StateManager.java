package org.atomnuke.task.threading;

/**
 *
 * @author zinic
 */
public class StateManager {

   private ExecutionManager.State state;

   public StateManager(ExecutionManager.State state) {
      this.state = state;
   }

   public synchronized ExecutionManager.State state() {
      return state;
   }

   public synchronized void update(ExecutionManager.State state) {
      this.state = state;
   }
}
