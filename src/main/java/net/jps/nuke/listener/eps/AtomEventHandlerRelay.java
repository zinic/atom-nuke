package net.jps.nuke.listener.eps;

import net.jps.nuke.listener.eps.eventlet.AtomEventlet;
import net.jps.nuke.listener.eps.selector.Selector;
import net.jps.nuke.task.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public interface AtomEventHandlerRelay {

   void enlistHandler(AtomEventlet handler) throws InitializationException;

   void enlistHandler(AtomEventlet handler, Selector selector) throws InitializationException;
}
