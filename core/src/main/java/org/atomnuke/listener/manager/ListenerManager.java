package org.atomnuke.listener.manager;

import java.util.List;
import org.atomnuke.context.InstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface ListenerManager {

   CancellationRemote addListener(InstanceContext<? extends AtomListener> atomListenerContext) throws InitializationException;

   boolean hasListeners();

   boolean isReentrant();

   List<ManagedListener> listeners();
}
