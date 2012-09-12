package org.atomnuke.util.config.update.listener;

import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public class ListenerContext<T> {

   private final ConfigurationListener<T> configurationListener;
   private final CancellationRemote cancellationRemote;

   public ListenerContext(ConfigurationListener<T> configurationListener, CancellationRemote cancellationRemote) {
      this.configurationListener = configurationListener;
      this.cancellationRemote = cancellationRemote;
   }

   public CancellationRemote cancellationRemote() {
      return cancellationRemote;
   }

   public ConfigurationListener<T> configurationListener() {
      return configurationListener;
   }
}
