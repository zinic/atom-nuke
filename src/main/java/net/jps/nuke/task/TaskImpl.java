package net.jps.nuke.task;

import net.jps.nuke.task.context.TaskContext;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import net.jps.nuke.listener.AtomListener;
import net.jps.nuke.listener.ReentrantAtomListener;
import net.jps.nuke.listener.RegisteredListener;
import net.jps.nuke.task.lifecycle.InitializationException;
import net.jps.nuke.util.TimeValue;
import net.jps.nuke.util.remote.AtomicCancellationRemote;
import net.jps.nuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public abstract class TaskImpl implements Task {

   private final List<RegisteredListener> assignedListeners;
   private final CancellationRemote cancelationRemote;
   private final AtomicBoolean reentrant;
   private final TaskContext context;
   private final TimeValue interval;
   private TimeValue timestamp;

   public TaskImpl(TaskContext context, TimeValue interval) {
      this(context, interval, new AtomicCancellationRemote());
   }

   public TaskImpl(TaskContext context, TimeValue interval, CancellationRemote cancelationRemote) {
      this.cancelationRemote = cancelationRemote;
      this.assignedListeners = new LinkedList<RegisteredListener>();
      this.context = context;
      this.interval = interval;
      this.timestamp = TimeValue.now();
      reentrant = new AtomicBoolean(true);
   }

   protected TaskContext taskContext() {
      return context;
   }

   protected void setTimestamp(TimeValue timestamp) {
      this.timestamp = timestamp;
   }

   protected List<RegisteredListener> listeners() {
      return assignedListeners;
   }

   @Override
   public boolean isReentrant() {
      return reentrant.get();
   }

   @Override
   public boolean canceled() {
      return cancelationRemote.canceled();
   }

   @Override
   public void cancel() {
      cancelationRemote.cancel();
   }

   @Override
   public CancellationRemote addListener(AtomListener listener) throws InitializationException {
      final CancellationRemote listenerCancelationRemote = new AtomicCancellationRemote();

      addListener(listener, listenerCancelationRemote);

      return listenerCancelationRemote;
   }

   @Override
   public synchronized void addListener(AtomListener listener, CancellationRemote listenerCancelationRemote) throws InitializationException {
      if (!(listener instanceof ReentrantAtomListener)) {
         reentrant.set(false);
      }

      try {
         listener.init(context);
         assignedListeners.add(new RegisteredListener(listenerCancelationRemote, context, listener));
      } catch (InitializationException ie) {
         throw ie;
      }
   }

   @Override
   public TimeValue interval() {
      return interval;
   }

   @Override
   public TimeValue nextPollTime() {
      return timestamp.add(interval());
   }
}
