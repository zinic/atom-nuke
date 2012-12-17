package org.atomnuke.task.threading;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.operation.SimpleOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class InstanceContextRunnableWrapper implements Runnable {

   private static final Logger LOG = LoggerFactory.getLogger(InstanceContextRunnableWrapper.class);
   private static final SimpleOperation<Runnable> RUNNABLE_OPERATION = new SimpleOperation<Runnable>() {
      @Override
      public void perform(Runnable instance) throws OperationFailureException {
         try {
            instance.run();
         } catch (Exception ex) {
            throw new OperationFailureException(ex);
         }
      }
   };
   
   private final InstanceContext<Runnable> runnableContext;

   public InstanceContextRunnableWrapper(InstanceContext<? extends Runnable> runnableContext) {
      this.runnableContext = (InstanceContext<Runnable>) runnableContext;
   }

   @Override
   public void run() {
      try {
         runnableContext.perform(RUNNABLE_OPERATION);
      } catch (OperationFailureException ofe) {
         LOG.error("Failed to run task: " + runnableContext + " - Reason: " + ofe.getCause().getMessage(), ofe.getCause());
      }
   }
}
