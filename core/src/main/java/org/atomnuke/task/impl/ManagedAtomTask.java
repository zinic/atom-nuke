package org.atomnuke.task.impl;

import java.util.UUID;
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
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.util.lifecycle.runnable.ReclaimableRunnable;
import org.atomnuke.util.lifecycle.operation.ReclaimOperation;
import org.atomnuke.util.result.ResultCatch;
import org.atomnuke.util.result.ResultCatchImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class ManagedAtomTask implements ReclaimableRunnable {

   private static final Logger LOG = LoggerFactory.getLogger(ManagedAtomTask.class);
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
   private final SinkManager sinkManager;
   private final Tasker tasker;

   public ManagedAtomTask(InstanceContext<AtomSource> atomSourceContext, SinkManager SinkManager, Tasker tasker) {
      this.atomSourceContext = atomSourceContext;
      this.sinkManager = SinkManager;
      this.tasker = tasker;
   }

   @Override
   public void run() {
      // Only poll if we have Sinks
      if (sinkManager.hasRegisteredSinks()) {
         try {
            final ResultCatch<AtomSourceResult> resultCatch = new ResultCatchImpl<AtomSourceResult>();
            atomSourceContext.perform(POLLING_OPERATION, resultCatch);

            if (resultCatch.hasResult()) {
               dispatchToSinks(resultCatch.result());
            }
         } catch (OperationFailureException ofe) {
            LOG.error("Failed to poll atom source: " + atomSourceContext + " - Reason: " + ofe.getMessage(), ofe);
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
         tasker.queueTask(new AtomSinkDriver(sink, driverArgument));
      }
   }
}
