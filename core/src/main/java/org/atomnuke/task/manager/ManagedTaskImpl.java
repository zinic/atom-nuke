package org.atomnuke.task.manager;

import java.util.UUID;
import org.atomnuke.context.InstanceContext;
import org.atomnuke.listener.RegisteredListener;
import org.atomnuke.listener.driver.AtomListenerDriver;
import org.atomnuke.listener.manager.ListenerManager;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceResult;
import org.atomnuke.task.Task;
import org.atomnuke.task.context.TaskContext;
import org.atomnuke.task.lifecycle.DestructionException;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ManagedTaskImpl implements ManagedTask {

   private static final Logger LOG = LoggerFactory.getLogger(ManagedTaskImpl.class);
   private final InstanceContext<? extends AtomSource> atomSourceContext;
   private final ExecutionManager executorService;
   private final ListenerManager listenerManager;
   private final Task task;
   private final UUID id;
   private TimeValue timestamp;

   public ManagedTaskImpl(Task task, ListenerManager listenerManager, TimeValue interval, ExecutionManager executorService, InstanceContext<? extends AtomSource> atomSourceContext) {
      this.task = task;

      this.listenerManager = listenerManager;
      this.executorService = executorService;
      this.atomSourceContext = atomSourceContext;

      timestamp = TimeValue.now();
      id = UUID.randomUUID();
   }

   @Override
   public boolean canceled() {
      return task.canceled();
   }

   @Override
   public void cancel() {
      task.cancel();
   }

   public boolean isReentrant() {
      return listenerManager.isReentrant();
   }

   @Override
   public UUID id() {
      return id;
   }

   @Override
   public void scheduled() {
      timestamp = TimeValue.now();
   }

   @Override
   public TimeValue nextPollTime() {
      return timestamp.add(task.interval());
   }

   @Override
   public void init(TaskContext taskContext) throws InitializationException {
      LOG.debug("Initializing task: " + task);
      
      atomSourceContext.stepInto();

      try {
         atomSourceContext.getInstance().init(taskContext);
      } finally {
         atomSourceContext.stepOut();
      }

      for (RegisteredListener registeredListener : listenerManager.listeners()) {
         registeredListener.listenerContext().stepInto();

         try {
            registeredListener.listenerContext().getInstance().init(taskContext);
         } finally {
            registeredListener.listenerContext().stepOut();
         }
      }
   }

   @Override
   public void destroy(TaskContext taskContext) {
      LOG.debug("Destroying task: " + task);

      for (RegisteredListener registeredListener : listenerManager.listeners()) {
         registeredListener.listenerContext().stepInto();

         try {
            registeredListener.listenerContext().getInstance().destroy(taskContext);
         } catch (DestructionException sde) {
            LOG.error(sde.getMessage(), sde);
         } finally {
            registeredListener.listenerContext().stepOut();
         }
      }
   }

   @Override
   public void run() {
      // Only poll if we have listeners
      if (listenerManager.hasListeners()) {
         atomSourceContext.stepInto();

         try {
            final AtomSourceResult pollResult = atomSourceContext.getInstance().poll();

            if (!pollResult.isEmpty()) {
               dispatchToListeners(pollResult);
            }
         } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
         } finally {
            atomSourceContext.stepOut();
         }
      }
   }

   private void dispatchToListeners(AtomSourceResult pollResult) {
      for (RegisteredListener listener : listenerManager.listeners()) {
         if (pollResult.isFeedPage()) {
            executorService.queue(new AtomListenerDriver(listener, pollResult.feed()));
         } else {
            executorService.queue(new AtomListenerDriver(listener, pollResult.entry()));
         }
      }
   }
}
