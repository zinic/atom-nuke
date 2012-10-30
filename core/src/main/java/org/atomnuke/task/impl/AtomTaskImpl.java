package org.atomnuke.task.impl;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.env.NopInstanceEnvironment;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.sink.manager.SinkManager;
import org.atomnuke.plugin.InstanceContextImpl;
import org.atomnuke.task.AtomTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class AtomTaskImpl implements AtomTask {

   private final SinkManager sinkManager;
   private final TaskHandle taskHandle;

   public AtomTaskImpl(SinkManager sinkManager, TaskHandle taskHandle) {
      this.sinkManager = sinkManager;
      this.taskHandle = taskHandle;
   }

   @Override
   public TaskHandle handle() {
      return taskHandle;
   }

   @Override
   public CancellationRemote addSink(AtomSink sink) {
      return addSink(new InstanceContextImpl<AtomSink>(NopInstanceEnvironment.getInstance(), sink));
   }

   @Override
   public CancellationRemote addSink(InstanceContext<? extends AtomSink> atomSinkContext) {
      return sinkManager.addSink(atomSinkContext);
   }
}
