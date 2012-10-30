package org.atomnuke.sink.manager;

import java.util.UUID;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ManagedSink {

   private final InstanceContext<AtomSink> sinkContext;
   private final CancellationRemote cancellationRemote;
   private final UUID taskId;

   public ManagedSink(InstanceContext<? extends AtomSink> sinkContext, CancellationRemote cancellationRemote, UUID taskId) {
      this.sinkContext = (InstanceContext<AtomSink>) sinkContext;
      this.cancellationRemote = cancellationRemote;
      this.taskId = taskId;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public UUID taskId() {
      return taskId;
   }

   public InstanceContext<AtomSink> sinkContext() {
      return sinkContext;
   }
}
