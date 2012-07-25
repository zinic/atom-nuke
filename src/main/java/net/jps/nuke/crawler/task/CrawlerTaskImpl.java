package net.jps.nuke.crawler.task;

import net.jps.nuke.crawler.remote.CancelationRemote;
import net.jps.nuke.crawler.remote.CancelationRemoteImpl;
import java.util.concurrent.TimeUnit;
import net.jps.nuke.util.TimeValue;
import net.jps.nuke.listener.FeedListener;

/**
 *
 * @author zinic
 */
public abstract class CrawlerTaskImpl implements CrawlerTask {

   private final CancelationRemote cancelationRemote;
   private final FeedListener listener;
   private TimeValue timestamp;
   private String location;

   public CrawlerTaskImpl(FeedListener listener) {
      this(new CancelationRemoteImpl(), listener);
   }

   public CrawlerTaskImpl(CancelationRemote cancelationRemote, FeedListener listener) {
      this.cancelationRemote = cancelationRemote;
      this.listener = listener;

      this.timestamp = new TimeValue(0, TimeUnit.MILLISECONDS);
      this.location = "";
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
   public String location() {
      return location;
   }

   @Override
   public FeedListener listener() {
      return listener;
   }

   @Override
   public TimeValue nextPollTime() {
      return timestamp.add(listener.listenerInterval());
   }

   @Override
   public void nextLocation(String location) {
      this.timestamp = TimeValue.now();
      this.location = location;
   }
}
