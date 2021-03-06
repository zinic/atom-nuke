package org.atomnuke.task.atom.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.context.LocalInstanceContext;
import org.atomnuke.plugin.context.NopInstanceContext;
import org.atomnuke.sink.manager.ManagedSink;
import org.atomnuke.sink.driver.AtomSinkDriver;
import org.atomnuke.sink.driver.DriverArgument;
import org.atomnuke.sink.manager.SinkManager;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.action.ActionType;
import org.atomnuke.source.action.AtomSourceActionImpl;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.source.result.AtomSourceResultImpl;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.util.lifecycle.runnable.ReclaimableTask;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;
import org.atomnuke.util.remote.CancellationRemote;
import org.atomnuke.util.result.ResultCatch;
import org.atomnuke.util.result.ResultCatchImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ManagedAtomTask implements ReclaimableTask {

   private static final Logger LOG = LoggerFactory.getLogger(ManagedAtomTask.class);
   private static final ComplexOperation<AtomSource, ResultCatch<AtomSourceResult>> POLLING_OPERATION = new ComplexOperation<AtomSource, ResultCatch<AtomSourceResult>>() {
      @Override
      public void perform(AtomSource instance, ResultCatch<AtomSourceResult> resultCatch) throws OperationFailureException {
         try {
            final AtomSourceResult pollResult = instance.poll();

            if (pollResult != null) {
               resultCatch.setResult(pollResult);
            } else {
               resultCatch.setResult(new AtomSourceResultImpl(new AtomSourceActionImpl(ActionType.HALT)));
            }
         } catch (AtomSourceException ase) {
            throw new OperationFailureException(ase);
         }
      }
   };
   
   private final SourceManagedPollingController pollingController;
   private final InstanceContext<AtomSource> atomSourceContext;
   private final SinkManager sinkManager;
   private final Tasker tasker;
   
   private CancellationRemote enlistedCancellationRemote;

   public ManagedAtomTask(SourceManagedPollingController pollingController, InstanceContext<AtomSource> atomSourceContext, SinkManager sinkManager, Tasker tasker) {
      this.pollingController = pollingController;
      this.atomSourceContext = atomSourceContext;
      this.sinkManager = sinkManager;
      this.tasker = tasker;
   }

   @Override
   public void enlisted(TaskHandle taskHandle) {
      enlistedCancellationRemote = taskHandle.cancellationRemote();
   }

   @Override
   public void run() {
      // Only poll if we have Sinks
      if (sinkManager.hasRegisteredSinks()) {
         final ResultCatch<AtomSourceResult> resultCatch = new ResultCatchImpl<AtomSourceResult>();

         try {
            atomSourceContext.perform(POLLING_OPERATION, resultCatch);
         } catch (OperationFailureException ofe) {
            LOG.error("Failed to poll atom source: " + atomSourceContext + " - Reason: " + ofe.getMessage(), ofe);
         }

         if (resultCatch.hasResult()) {
            handlePollingResult(resultCatch);
         }
      }
   }

   @Override
   public void destroy() {
      try {
         atomSourceContext.perform(ReclaimOperation.<AtomSource>instance());
      } catch (OperationFailureException ofe) {
         LOG.error("Failed to destroy atom source: " + atomSourceContext + " - Reason: " + ofe.getMessage(), ofe);
      }
   }

   private void dispatchToSinks(AtomSourceResult pollResult) {
      final DriverArgument driverArgument = new DriverArgument(pollResult.feed(), pollResult.entry());

      for (ManagedSink sink : sinkManager.sinks()) {
         tasker.queueAction(new NopInstanceContext(new AtomSinkDriver(sink, driverArgument)));
      }
   }

   private void handlePollingResult(final ResultCatch<AtomSourceResult> resultCatch) {
      switch (resultCatch.result().action().action()) {
         case HAS_NEXT:
            // If we have more on this source, ignore our schedule to catch up
            pollingController.ignoreSchedule();

            // Dispatch our payload to the execution pool
            dispatchToSinks(resultCatch.result());
            break;

         case HALT:
            // If we need to halt we cancel our task and then tell our pooling controller to honor the schedule
            enlistedCancellationRemote.cancel();

         case SLEEP:
         default:
            pollingController.honorSchedule();
      }
   }
}
