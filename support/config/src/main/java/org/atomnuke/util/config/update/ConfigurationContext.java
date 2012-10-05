package org.atomnuke.util.config.update;

import org.atomnuke.util.config.ConfigurationException;
import org.atomnuke.util.config.update.listener.ConfigurationListener;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface ConfigurationContext<T> {

   CancellationRemote addListener(ConfigurationListener<T> listener) throws ConfigurationException;

   CancellationRemote cancellationRemote();
}
