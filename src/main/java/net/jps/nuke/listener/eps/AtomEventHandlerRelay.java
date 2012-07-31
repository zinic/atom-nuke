package net.jps.nuke.listener.eps;

import net.jps.nuke.listener.eps.handler.AtomEventlet;
import net.jps.nuke.listener.eps.selector.Selector;
import net.jps.nuke.service.InitializationException;

/**
 *
 * @author zinic
 */
public interface AtomEventHandlerRelay {

   void enlistHandler(AtomEventlet handler) throws InitializationException;

   void enlistHandler(AtomEventlet handler, Selector selector) throws InitializationException;
}
