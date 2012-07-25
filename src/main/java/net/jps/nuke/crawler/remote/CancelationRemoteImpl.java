package net.jps.nuke.crawler.remote;

/**
 *
 * @author zinic
 */
public class CancelationRemoteImpl implements CancelationRemote {

   private boolean canceled;

   public CancelationRemoteImpl() {
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
