package org.atomnuke.task.threading;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Future;
import org.atomnuke.service.ServiceContext;

/**
 *
 * @author zinic
 */
public class ExecutionManagerImpl implements ExecutionManager {

   private final Map<Long, TrackedFuture> taskFutures;
   private final ExecutionQueue executionQueue;
   private final StateManager stateManager;

   public ExecutionManagerImpl(ExecutionQueue executionQueue) {
      this.executionQueue = executionQueue;

      stateManager = new StateManager(State.NEW);
      taskFutures = new TreeMap<Long, TrackedFuture>();
   }

   private synchronized Map<Long, TrackedFuture> taskFutures() {
      for (Iterator<TrackedFuture> itr = taskFutures.values().iterator(); itr.hasNext();) {
         final TrackedFuture nextTask = itr.next();

         if (nextTask.done()) {
            itr.remove();
         }
      }

      return taskFutures;
   }

   @Override
   public void init(ServiceContext sc) {
      stateManager.update(State.STARTING);
      stateManager.update(State.READY);
   }

   @Override
   public void destroy() {
      stateManager.update(State.STOPPING);

      // Shut down the execution queue we're using
      executionQueue.destroy();

      stateManager.update(State.DESTROYED);
   }

   @Override
   public synchronized Future submit(Runnable r) {
      return executionQueue.submit(r);
   }
   
   @Override
   public synchronized TrackedFuture submitTracked(long taskId, Runnable r) {
      final TrackedFuture taskFuture = new TrackedFuture(executionQueue.submit(r), taskId);
      taskFutures.put(taskId, taskFuture);

      return taskFuture;
   }

   @Override
   public State state() {
      final State state = stateManager.state();

      switch (state) {
         case STOPPING:
         case DESTROYED:
            break;

         case DRAINING:
            if (!executionQueue.isFull()) {
               stateManager.update(State.READY);
            }
            break;

         default:
            if (executionQueue.isFull()) {
               stateManager.update(State.DRAINING);
            }
      }

      return stateManager.state();
   }

   @Override
   public boolean submitted(long taskId) {
      return taskFutures().containsKey(taskId);
   }
}
