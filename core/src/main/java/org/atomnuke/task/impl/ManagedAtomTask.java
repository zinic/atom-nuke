package org.atomnuke.task.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.listener.manager.ManagedListener;
import org.atomnuke.listener.driver.AtomListenerDriver;
import org.atomnuke.listener.manager.ListenerManager;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.source.result.ResultType;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.result.ResultCatch;
import org.atomnuke.util.result.ResultCatchImpl;

/**
 *
 * @author zinic
 */
public class ManagedAtomTask extends AbstractManagedTask {

   private static final ComplexOperation<AtomSource, ResultCatch<AtomSourceResult>> POLLING_OPERATION = new ComplexOperation<AtomSource, ResultCatch<AtomSourceResult>>() {
      @Override
      public void perform(AtomSource instance, ResultCatch<AtomSourceResult> resultCatch) throws OperationFailureException {
         try {
            final AtomSourceResult pollResult = instance.poll();

            if (pollResult.type() != ResultType.EMPTY) {
               resultCatch.setResult(pollResult);
            }
         } catch (AtomSourceException ase) {
            throw new OperationFailureException(ase);
         }
      }
   };

   private final InstanceContext<AtomSource> atomSourceContext;
   private final ExecutionManager executorService;
   private final ListenerManager listenerManager;

   public ManagedAtomTask(TaskHandle taskHandle, InstanceContext<AtomSource> atomSourceContext, ExecutionManager executorService, ListenerManager listenerManager) {
      super(taskHandle);

      this.atomSourceContext = atomSourceContext;
      this.executorService = executorService;
      this.listenerManager = listenerManager;
   }

   @Override
   public void run() {
      // Only poll if we have listeners
      if (listenerManager.hasListeners()) {
         final ResultCatch<AtomSourceResult> resultCatch = new ResultCatchImpl<AtomSourceResult>();
         atomSourceContext.perform(POLLING_OPERATION, resultCatch);

         if (resultCatch.hasResult()) {
            dispatchToListeners(resultCatch.result());
         }
      }
   }

   private void dispatchToListeners(AtomSourceResult pollResult) {
      for (ManagedListener listener : listenerManager.listeners()) {
         if (pollResult.type() == ResultType.FEED) {
            executorService.submit(listener.parentHandle(), new AtomListenerDriver(listener, pollResult.feed()));
         } else {
            executorService.submit(listener.parentHandle(), new AtomListenerDriver(listener, pollResult.entry()));
         }
      }
   }
}
