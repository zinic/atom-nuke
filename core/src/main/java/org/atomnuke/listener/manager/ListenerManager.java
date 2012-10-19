package org.atomnuke.listener.manager;

import java.util.List;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface ListenerManager {

   CancellationRemote addListener(InstanceContext<? extends AtomListener> atomListenerContext);

   boolean hasListeners();

   List<ManagedListener> listeners();
}
