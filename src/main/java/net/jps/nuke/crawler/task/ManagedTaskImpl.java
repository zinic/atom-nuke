package net.jps.nuke.crawler.task;

import net.jps.nuke.crawler.task.CrawlerTaskImpl;
import net.jps.nuke.crawler.remote.CancelationRemote;
import net.jps.nuke.crawler.remote.CancelationRemoteImpl;
import java.util.UUID;
import net.jps.nuke.listener.FeedListener;

/**
 *
 * @author zinic
 */
public class ManagedTaskImpl extends CrawlerTaskImpl implements ManagedTask {

   private final UUID id;

   public ManagedTaskImpl(FeedListener listener) {
      this(new CancelationRemoteImpl(), listener);
   }

   public ManagedTaskImpl(CancelationRemote cancelationRemote, FeedListener listener) {
      super(cancelationRemote, listener);

      this.id = UUID.randomUUID();
   }

   @Override
   public UUID id() {
      return id;
   }

   @Override
   public int hashCode() {
      int hash = 5;
      hash = 59 * hash + id().hashCode();
      return hash;
   }

   @Override
   public boolean equals(Object obj) {
      if (obj != null && ManagedTask.class == obj.getClass()) {
         final ManagedTask other = (ManagedTask) obj;

         return id().equals(other.id());
      }

      return false;
   }
}
