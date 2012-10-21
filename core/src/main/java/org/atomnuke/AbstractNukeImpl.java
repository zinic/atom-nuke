package org.atomnuke;

import java.util.UUID;
import org.atomnuke.kernel.GenericKernelDelegate;
import org.atomnuke.kernel.shutdown.ShutdownHook;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.plugin.local.LocalInstanceEnvironment;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.AtomTask;
import org.atomnuke.util.TimeValue;
import org.atomnuke.util.lifecycle.InitializationException;
import org.atomnuke.util.lifecycle.Reclaimable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public abstract class AbstractNukeImpl implements Nuke {

   private static final Logger LOG = LoggerFactory.getLogger(AbstractNukeImpl.class);
   private static final long MAX_WAIT_TIME_FOR_SHUTDOWN = 15000;

   private final GenericKernelDelegate kernelDelegate;
   private final ShutdownHook kernelShutdownHook;
   private final Thread controlThread;

   public AbstractNukeImpl(ShutdownHook kernelShutdownHook, GenericKernelDelegate kernelDelegate) {
      this.kernelShutdownHook = kernelShutdownHook;
      this.kernelDelegate = kernelDelegate;

      this.controlThread = new Thread(kernelDelegate, "nuke-kernel-" + UUID.randomUUID().toString());
   }

   @Override
   public ShutdownHook shutdownHook() {
      return kernelShutdownHook;
   }

   /**
    * Helper method for following a given source at a defined polling interval.
    * This has the same effect as calling follow on the Tasker interface.This
    * calls the follow method by wrapping the given AtomSource in a
    * SimpleInstanceContext.
    *
    * @param source the AtomSource to be scheduled.
    * @param pollingInterval the desired polling interval for the source.
    * @return a new task instance for further interaction with the newly
    * scheduled source.
    * @throws InitializationException thrown when initializing the source fails
    * wit the current task context.
    */
   public AtomTask follow(AtomSource source, TimeValue pollingInterval) {
      return follow(new InstanceContextImpl<AtomSource>(LocalInstanceEnvironment.getInstance(), source), pollingInterval);
   }

   /**
    * Helper method for following a given source at a defined polling interval.
    * This has the same effect as calling follow on the Tasker interface.
    *
    * @param source the instance context of the AtomSource to be scheduled.
    * @param pollingInterval the desired polling interval for the source.
    * @return a new task instance for further interaction with the newly
    * scheduled source.
    * @throws InitializationException thrown when initializing the source fails
    * wit the current task context.
    */
   public AtomTask follow(InstanceContext<AtomSource> source, TimeValue pollingInterval) {
      return atomTasker().follow(source, pollingInterval);
   }

   @Override
   public void start() {
      if (controlThread.getState() != Thread.State.NEW) {
         throw new IllegalStateException("Crawler already started or destroyed.");
      }

      kernelShutdownHook.enlist(new Reclaimable() {
         @Override
         public void destroy() {
            kernelDelegate.cancellationRemote().cancel();
            kernelDelegate.taskManager().destroy();

            try {
               controlThread.join(MAX_WAIT_TIME_FOR_SHUTDOWN);
            } catch (InterruptedException ie) {
               LOG.info("Nuke kernel interrupted while shutting down. Killing thread now.", ie);

               controlThread.interrupt();
            }
         }
      });

      LOG.info("Nuke kernel: " + toString() + " starting.");
      controlThread.start();
   }

   @Override
   public void destroy() {
      kernelShutdownHook.shutdown();
   }
}
