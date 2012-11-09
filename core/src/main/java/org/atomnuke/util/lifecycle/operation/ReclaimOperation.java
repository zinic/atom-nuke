package org.atomnuke.util.lifecycle.operation;

import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.plugin.operation.SimpleOperation;
import org.atomnuke.lifecycle.Reclaimable;

/**
 *
 * @author zinic
 */
public class ReclaimOperation implements SimpleOperation<Reclaimable> {

   private static final SimpleOperation<Reclaimable> INSTANCE = new ReclaimOperation();

   public static <T extends Reclaimable> SimpleOperation<T> instance() {
      return (SimpleOperation<T>) INSTANCE;
   }

   @Override
   public void perform(Reclaimable instance) throws OperationFailureException {
      instance.destroy();
   }
}
