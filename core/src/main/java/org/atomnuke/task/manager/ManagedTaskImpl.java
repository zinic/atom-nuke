package org.atomnuke.task.manager;

import java.util.UUID;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.listener.manager.ManagedListener;
import org.atomnuke.listener.driver.AtomListenerDriver;
import org.atomnuke.listener.manager.ListenerManager;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.operation.SimpleOperation;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.source.result.ResultType;
import org.atomnuke.task.AtomTask;
import org.atomnuke.task.operation.TaskLifeCycleDestroyOperation;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ManagedTaskImpl implements ManagedTask {

   private final InstanceContext<AtomSource> atomSourceContext;
   private final SimpleOperation<AtomSource> pollingOperation;
   private final ExecutionManager executorService;
   private final ListenerManager listenerManager;
   private final AtomTask task;
   private TimeValue timestamp;

   public ManagedTaskImpl(AtomTask task, ListenerManager listenerManager, TimeValue interval, ExecutionManager executorService, InstanceContext<AtomSource> atomSourceContext) {
      this.task = task;

      this.listenerManager = listenerManager;
      this.executorService = executorService;
      this.atomSourceContext = atomSourceContext;

      pollingOperation = new SimpleOperation<AtomSource>() {
         @Override
         public void perform(AtomSource instance) throws OperationFailureException {
            try {
               final AtomSourceResult pollResult = instance.poll();

               if (pollResult.type() != ResultType.EMPTY) {
                  dispatchToListeners(pollResult);
               }
            } catch (AtomSourceException ase) {
               throw new OperationFailureException(ase);
            }
         }
      };

      timestamp = TimeValue.now();
   }

   @Override
   public CancellationRemote cancellationRemote() {
      return task.cancellationRemote();
   }

   public boolean isReentrant() {
      return listenerManager.isReentrant();
   }

   @Override
   public UUID id() {
      return task.id();
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
   public void destroy() {
      for (ManagedListener listener : listenerManager.listeners()) {
         listener.listenerContext().perform(TaskLifeCycleDestroyOperation.<AtomListener>instance());
      }
   }

   @Override
   public void run() {
      // Only poll if we have listeners
      if (listenerManager.hasListeners()) {
         atomSourceContext.perform(pollingOperation);
      }
   }

   private void dispatchToListeners(AtomSourceResult pollResult) {
      for (ManagedListener listener : listenerManager.listeners()) {
         if (pollResult.type() == ResultType.FEED) {
            executorService.submit(new AtomListenerDriver(listener, pollResult.feed()));
         } else {
            executorService.submit(new AtomListenerDriver(listener, pollResult.entry()));
         }
      }
   }
}
