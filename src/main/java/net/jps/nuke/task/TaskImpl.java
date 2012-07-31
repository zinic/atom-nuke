package net.jps.nuke.task;

import net.jps.nuke.listener.RegisteredListener;
import java.util.LinkedList;
import java.util.List;
import net.jps.nuke.util.remote.CancellationRemote;
import net.jps.nuke.util.remote.AtomicCancellationRemote;
import net.jps.nuke.listener.AtomListener;
import net.jps.nuke.util.TimeValue;

/**
 *
 * @author zinic
 */
public abstract class TaskImpl implements Task {

   private final List<RegisteredListener> assignedListeners;
   private final CancellationRemote cancelationRemote;
   private final TimeValue interval;
   private TimeValue timestamp;

   public TaskImpl(TimeValue interval) {
      this(interval, new AtomicCancellationRemote());
   }

   public TaskImpl(TimeValue interval, CancellationRemote cancelationRemote) {
      this.cancelationRemote = cancelationRemote;
      this.assignedListeners = new LinkedList<RegisteredListener>();

      this.interval = interval;
      this.timestamp = TimeValue.now();
   }

   protected void setTimestamp(TimeValue timestamp) {
      this.timestamp = timestamp;
   }

   protected List<RegisteredListener> listeners() {
      return assignedListeners;
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
   public CancellationRemote addListener(AtomListener listener) {
      final CancellationRemote listenerCancelationRemote = new AtomicCancellationRemote();
      addListener(listener, listenerCancelationRemote);

      return listenerCancelationRemote;
   }

   @Override
   public synchronized void addListener(AtomListener listener, CancellationRemote listenerCancelationRemote) {
      assignedListeners.add(new RegisteredListener(listener, listenerCancelationRemote));
   }

   @Override
   public TimeValue interval() {
      return interval;
   }

   @Override
   public TimeValue nextPollTime() {
//      System.out.println("Interval: " + interval + " - Timestamp: " + timestamp + " - Addition Result: " + timestamp.add(interval()));
      
      return timestamp.add(interval());
   }
}
