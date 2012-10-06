package org.atomnuke.listener.manager;

import java.util.List;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.plugin.Environment;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface ListenerManager {

   CancellationRemote addListener(Environment environment, AtomListener atomListener);

   boolean hasListeners();

   boolean isReentrant();

   List<ManagedListener> listeners();
}
