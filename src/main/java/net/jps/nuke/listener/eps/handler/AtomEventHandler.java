package net.jps.nuke.listener.eps.handler;

import net.jps.nuke.atom.model.Entry;

/**
 *
 * @author zinic
 */
public interface AtomEventHandler {

   void entry(Entry entry) throws EventProcessingException;
}
