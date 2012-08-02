package net.jps.nuke.listener.manager;

import java.util.List;
import net.jps.nuke.listener.AtomListener;
import net.jps.nuke.listener.RegisteredListener;
import net.jps.nuke.task.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public interface ListenerManager {

   void addListener(AtomListener atomListener) throws InitializationException;

   boolean hasListeners();

   boolean isReentrant();
   
   List<RegisteredListener> listeners();
}
