package net.jps.nuke.crawler.remote;

/**
 *
 * @author zinic
 */
public class CancellationRemoteImpl implements CancellationRemote {

   private boolean canceled;

   public CancellationRemoteImpl() {
      canceled = false;
   }

   @Override
   public synchronized boolean canceled() {
      return canceled;
   }

   @Override
   public synchronized void cancel() {
      canceled = true;
   }
}
