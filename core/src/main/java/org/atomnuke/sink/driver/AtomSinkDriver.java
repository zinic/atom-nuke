package org.atomnuke.sink.driver;

import org.atomnuke.sink.AtomSink;
import org.atomnuke.sink.AtomSinkException;
import org.atomnuke.sink.AtomSinkResult;
import org.atomnuke.sink.manager.ManagedSink;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.util.lifecycle.runnable.ReclaimableRunnable;

/**
 *
 * @author zinic
 */
public class AtomSinkDriver implements ReclaimableRunnable {

   private static final ComplexOperation<AtomSink, DriverArgument> DRIVER_OPERATION = new ComplexOperation<AtomSink, DriverArgument>() {
      @Override
      public void perform(AtomSink instance, DriverArgument argument) throws OperationFailureException {
         try {
            if (argument.feed() != null) {
               argument.setCapturedResult(instance.feedPage(argument.feed()));
            } else if (argument.entry() != null) {
               argument.setCapturedResult(instance.entry(argument.entry()));
            } else {
               argument.setCapturedResult(AtomSinkResult.halt("Feed document was null."));
            }
         } catch (AtomSinkException ale) {
            throw new OperationFailureException(ale);
         }
      }
   };
   
   private final ManagedSink registeredSink;
   private final DriverArgument driverArgument;

   public AtomSinkDriver(ManagedSink registeredSink, DriverArgument driverArgument) {
      this.registeredSink = registeredSink;
      this.driverArgument = driverArgument;
   }

   @Override
   public void run() {
      registeredSink.sinkContext().perform(DRIVER_OPERATION, driverArgument);

      switch (driverArgument.result().action()) {
         case HALT:
            registeredSink.cancellationRemote().cancel();
            break;

         default:
      }
   }

   @Override
   public void destroy() {
   }
}
