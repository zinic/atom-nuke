package net.jps.nuke.crawler.task;

import net.jps.nuke.crawler.remote.CancellationRemote;
import net.jps.nuke.listener.FeedListener;

/**
 *
 * @author zinic
 */
public class RegisteredListener {

   private final CancellationRemote cancellationRemote;
   private final FeedListener listener;

   public RegisteredListener(FeedListener listener, CancellationRemote cancellationRemote) {
      this.cancellationRemote = cancellationRemote;
      this.listener = listener;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public FeedListener listener() {
      return listener;
   }
}
