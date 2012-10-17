package org.atomnuke.listener.driver;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.AtomListenerException;
import org.atomnuke.listener.AtomListenerResult;
import org.atomnuke.listener.manager.ManagedListener;
import org.atomnuke.plugin.operation.ComplexOperation;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class AtomListenerDriver implements RegisteredListenerDriver {

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

   public AtomListenerDriver(ManagedListener registeredListener, Entry entry) {
      this(registeredListener, null, entry);
   }

   public AtomListenerDriver(ManagedListener registeredListener, Feed feed) {
      this(registeredListener, feed, null);
   }

   private AtomListenerDriver(ManagedListener registeredListener, Feed feed, Entry entry) {
      this.registeredListener = registeredListener;
      this.driverArgument = new DriverArgument(feed, entry);
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
