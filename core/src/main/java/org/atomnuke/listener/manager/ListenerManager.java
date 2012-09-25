package org.atomnuke.listener.manager;

import java.util.List;
import org.atomnuke.plugin.InstanceEnvironment;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface ListenerManager {

   CancellationRemote addListener(InstanceEnvironment<? extends AtomListener> atomListenerContext);

   boolean hasListeners();

   boolean isReentrant();

   List<ManagedListener> listeners();
}
