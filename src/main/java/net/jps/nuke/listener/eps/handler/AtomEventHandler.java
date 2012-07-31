package net.jps.nuke.listener.eps.handler;

import net.jps.nuke.atom.model.Entry;
import net.jps.nuke.listener.eps.EventProcessingException;

/**
 *
 * @author zinic
 */
public interface AtomEventHandler {

   void entry(Entry entry) throws EventProcessingException;
}
