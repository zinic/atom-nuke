package net.jps.nuke.listener.eps;

import net.jps.nuke.listener.eps.handler.AtomEventHandler;
import net.jps.nuke.listener.eps.handler.AtomEventHandlerException;
import net.jps.nuke.listener.eps.handler.Selector;

/**
 *
 * @author zinic
 */
public interface AtomEventHandlerRelay {

   void enlistHandler(AtomEventHandler handler, Selector selector) throws AtomEventHandlerException;
}
