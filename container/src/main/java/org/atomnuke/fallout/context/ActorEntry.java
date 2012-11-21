package org.atomnuke.fallout.context;

import org.atomnuke.lifecycle.Reclaimable;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.operation.OperationFailureException;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.context.AtomTaskContext;
import org.atomnuke.task.operation.TaskLifeCycleInitOperation;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ActorEntry {

   private final InstanceContext<? extends Reclaimable> instanceContext;
   private CancellationRemote cancellationRemote;
   private boolean initialized;

   public ActorEntry(InstanceContext<? extends Reclaimable> actorInstanceContext) {
      this.instanceContext = actorInstanceContext;
      initialized = false;
   }

   public Class instanceClass() {
      return instanceContext.instance().getClass();
   }

   public InstanceContext<? extends Reclaimable> instanceContext() {
      return instanceContext;
   }

   public void cancel() {
      if (cancellationRemote != null) {
         cancellationRemote.cancel();
      }
   }

   public void setCancellationRemote(CancellationRemote cancellationRemote) {
      this.cancellationRemote = cancellationRemote;
   }

   public synchronized void init(AtomTaskContext atc) throws OperationFailureException {
      if (!initialized) {
         ((InstanceContext<AtomSource>) instanceContext).perform(TaskLifeCycleInitOperation.<AtomSource>instance(), atc);
         initialized = true;
      }
   }

   public synchronized boolean initialized() {
      return initialized;
   }

   @Override
   public String toString() {
      return "ActorEntry{" + "instanceContext=" + instanceContext + ", cancellationRemote=" + cancellationRemote + ", initialized=" + initialized + '}';
   }
}
