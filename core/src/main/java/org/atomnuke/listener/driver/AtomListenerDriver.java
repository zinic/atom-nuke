package org.atomnuke.listener.driver;

import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.AtomListenerException;
import org.atomnuke.listener.AtomListenerResult;
import org.atomnuke.listener.manager.ManagedListener;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;

/**
 *
 * @author zinic
 */
public class AtomListenerDriver implements Runnable {

   private static final ComplexOperation<AtomListener, DriverArgument> DRIVER_OPERATION = new ComplexOperation<AtomListener, DriverArgument>() {
      @Override
      public void perform(AtomListener instance, DriverArgument argument) throws OperationFailureException {
         try {
            if (argument.feed() != null) {
               argument.setCapturedResult(instance.feedPage(argument.feed()));
            } else if (argument.entry() != null) {
               argument.setCapturedResult(instance.entry(argument.entry()));
            } else {
               argument.setCapturedResult(AtomListenerResult.halt("Feed document was null."));
            }
         } catch (AtomListenerException ale) {
            throw new OperationFailureException(ale);
         }
      }
   };

   private final ManagedListener registeredListener;
   private final DriverArgument driverArgument;

   public AtomListenerDriver(ManagedListener registeredListener, DriverArgument driverArgument) {
      this.registeredListener = registeredListener;
      this.driverArgument = driverArgument;
   }

   @Override
   public void run() {
      registeredListener.listenerContext().perform(DRIVER_OPERATION, driverArgument);

      switch (driverArgument.result().action()) {
         case HALT:
            registeredListener.cancellationRemote().cancel();
            break;

         default:
      }
   }
}
