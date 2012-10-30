package org.atomnuke.task.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.sink.manager.ManagedSink;
import org.atomnuke.sink.driver.AtomSinkDriver;
import org.atomnuke.sink.driver.DriverArgument;
import org.atomnuke.sink.manager.SinkManager;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.source.AtomSource;
import org.atomnuke.source.AtomSourceException;
import org.atomnuke.source.result.AtomSourceResult;
import org.atomnuke.source.result.ResultType;
import org.atomnuke.task.ReclaimableRunnable;
import org.atomnuke.task.threading.ExecutionManager;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;
import org.atomnuke.util.result.ResultCatch;
import org.atomnuke.util.result.ResultCatchImpl;

/**
 *
 * @author zinic
 */
public class ManagedAtomTask implements ReclaimableRunnable {

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
   private final ExecutionManager executionManager;
   private final SinkManager SinkManager;

   public ManagedAtomTask(InstanceContext<AtomSource> atomSourceContext, ExecutionManager executionManager, SinkManager SinkManager) {
      this.atomSourceContext = atomSourceContext;
      this.executionManager = executionManager;
      this.SinkManager = SinkManager;
   }

   @Override
   public void run() {
      // Only poll if we have Sinks
      if (SinkManager.hasRegisteredSinks()) {
         final ResultCatch<AtomSourceResult> resultCatch = new ResultCatchImpl<AtomSourceResult>();
         atomSourceContext.perform(POLLING_OPERATION, resultCatch);

         if (resultCatch.hasResult()) {
            dispatchToSinks(resultCatch.result());
         }
      }
   }

   @Override
   public void destroy() {
      atomSourceContext.perform(ReclaimOperation.<AtomSource>instance());
   }

   private void dispatchToSinks(AtomSourceResult pollResult) {
      final DriverArgument driverArgument = new DriverArgument(pollResult.feed(), pollResult.entry());

      for (ManagedSink Sink : SinkManager.sinks()) {
         executionManager.submit(Sink.taskId(), new AtomSinkDriver(Sink, driverArgument));
      }
   }
}
