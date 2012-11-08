package org.atomnuke.task.manager.impl;

import org.atomnuke.task.impl.ManagedAtomTask;
import org.atomnuke.task.manager.Tasker;
import org.atomnuke.sink.manager.SinkManager;
import org.atomnuke.sink.manager.SinkManagerImpl;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.plugin.LocalInstanceContext;
import org.atomnuke.service.gc.ReclamationHandler;
import org.atomnuke.source.AtomSource;
import org.atomnuke.task.atom.AtomTask;
import org.atomnuke.task.TaskHandle;
import org.atomnuke.task.impl.AtomTaskImpl;
import org.atomnuke.task.manager.AtomTasker;
import org.atomnuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public class AtomTaskerImpl implements AtomTasker {

   private final ReclamationHandler reclamationHandler;
   private final Tasker tasker;

   public AtomTaskerImpl(ReclamationHandler reclamationHandler, Tasker tasker) {
      this.reclamationHandler = reclamationHandler;
      this.tasker = tasker;
   }

   @Override
   public AtomTask follow(InstanceContext<AtomSource> source, TimeValue pollingInterval) {
      // New Sink manager
      final SinkManager sinkManager = new SinkManagerImpl(reclamationHandler);

      // Register and track the new source
      final ManagedAtomTask managedAtomTask = new ManagedAtomTask(source, sinkManager, tasker);
      final TaskHandle newHandle = tasker.pollTask(new LocalInstanceContext(managedAtomTask), pollingInterval);

      return new AtomTaskImpl(sinkManager, newHandle);
   }
}
