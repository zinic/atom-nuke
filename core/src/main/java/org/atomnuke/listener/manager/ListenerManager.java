package org.atomnuke.listener.manager;

import java.util.List;
import org.atomnuke.context.InstanceContext;
import org.atomnuke.listener.AtomListener;
import org.atomnuke.listener.RegisteredListener;
import org.atomnuke.task.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public interface ListenerManager {

   void addListener(AtomListener atomListener) throws InitializationException;

   void addListener(InstanceContext<? extends AtomListener> atomListenerContext) throws InitializationException;

   boolean hasListeners();

   boolean isReentrant();

   List<RegisteredListener> listeners();
}
