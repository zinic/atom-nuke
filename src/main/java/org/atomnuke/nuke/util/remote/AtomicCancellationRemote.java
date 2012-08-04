package org.atomnuke.nuke.util.remote;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author zinic
 */
public class AtomicCancellationRemote implements CancellationRemote {

   private final AtomicBoolean canceled;

   public AtomicCancellationRemote() {
      canceled = new AtomicBoolean(false);
   }

   @Override
   public boolean canceled() {
      return canceled.get();
   }

   @Override
   public void cancel() {
      canceled.set(true);
   }
}
