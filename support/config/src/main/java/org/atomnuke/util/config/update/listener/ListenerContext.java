package org.atomnuke.util.config.update.listener;

import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.config.io.UpdateTag;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ListenerContext<T> {

   private final InstanceContext<ConfigurationListener<T>> configurationListener;
   private final CancellationRemote cancellationRemote;
   private UpdateTag lastUpdateTagSeen;

   public ListenerContext(InstanceContext<ConfigurationListener<T>> configurationListener, CancellationRemote cancellationRemote) {
      this.configurationListener = configurationListener;
      this.cancellationRemote = cancellationRemote;
   }

   public UpdateTag lastUpdateTagSeen() {
      return lastUpdateTagSeen;
   }

   public void setLastUpdateTagSeen(UpdateTag lastUpdateTagSeen) {
      this.lastUpdateTagSeen = lastUpdateTagSeen;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public InstanceContext<ConfigurationListener<T>> configurationListener() {
      return configurationListener;
   }
}
