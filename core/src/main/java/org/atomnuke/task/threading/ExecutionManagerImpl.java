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

   private final Map<UUID, TaskFuture> taskFutures;
   private final ExecutionQueue executionQueue;
   private final StateManager stateManager;

   public ExecutionManagerImpl() {
      this(new ExecutionQueueImpl());
   }

   public ExecutionManagerImpl(ExecutionQueue executionQueue) {
      this.executionQueue = executionQueue;

      stateManager = new StateManager(State.NEW);
      taskFutures = new TreeMap<UUID, TaskFuture>();
   }

   private synchronized Map<UUID, TaskFuture> taskFutures() {
      for (Iterator<TaskFuture> itr = taskFutures.values().iterator(); itr.hasNext();) {
         final TaskFuture nextTask = itr.next();

         if (nextTask.done()) {
            itr.remove();
         }
      }

      return taskFutures;
   }

   @Override
   public void init(ServiceContext sc) {
      stateManager.update(State.STARTING);
      stateManager.update(State.OK);
   }

   @Override
   public void destroy() {
      stateManager.update(State.STOPPING);

      // Shut down the execution queue we're using
      executionQueue.destroy();

      stateManager.update(State.DESTROYED);
   }

   @Override
   public TaskFuture submit(Runnable r) {
      return submit(UUID.randomUUID(), r);
   }

   @Override
   public synchronized TaskFuture submit(UUID id, Runnable r) {
      final TaskFuture taskFuture = new TaskFuture(executionQueue.submit(r), id);
      taskFutures.put(id, taskFuture);

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
               stateManager.update(State.OK);
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
   public boolean submitted(UUID id) {
      return taskFutures().containsKey(id);
   }
}
