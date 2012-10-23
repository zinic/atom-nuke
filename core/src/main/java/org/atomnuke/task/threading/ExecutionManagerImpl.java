package org.atomnuke.task.threading;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import org.atomnuke.service.context.ServiceContext;

/**
 *
 * @author zinic
 */
public class ExecutionManagerImpl implements ExecutionManager {

   private final Map<UUID, ExecutionFuture> taskFutures;
   private final ExecutionQueue executionQueue;
   private final StateManager stateManager;

   public ExecutionManagerImpl() {
      this(new ExecutionQueueImpl());
   }

   public ExecutionManagerImpl(ExecutionQueue executionQueue) {
      this.executionQueue = executionQueue;

      stateManager = new StateManager(State.NEW);
      taskFutures = new TreeMap<UUID, ExecutionFuture>();
   }

   private synchronized Map<UUID, ExecutionFuture> taskFutures() {
      for (Iterator<ExecutionFuture> itr = taskFutures.values().iterator(); itr.hasNext();) {
         final ExecutionFuture nextTask = itr.next();

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
   public synchronized ExecutionFuture submit(UUID taskId, Runnable r) {
      final ExecutionFuture taskFuture = new ExecutionFuture(executionQueue.submit(r), taskId);
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
   public boolean submitted(UUID taskId) {
      return taskFutures().containsKey(taskId);
   }
}
