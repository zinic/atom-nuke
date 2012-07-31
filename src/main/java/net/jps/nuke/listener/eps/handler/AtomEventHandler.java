package net.jps.nuke.listener.eps.handler;

import net.jps.nuke.atom.model.Entry;

/**
 *
 * @author zinic
 */
public interface AtomEventHandler {

   void init() throws AtomEventHandlerException;

   void destroy() throws AtomEventHandlerException;

   void entry(Entry entry) throws AtomEventHandlerException;
}
