package org.atomnuke.listener.eps;

import org.atomnuke.context.InstanceContext;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.listener.eps.selector.Selector;
import org.atomnuke.task.lifecycle.InitializationException;

/**
 *
 * @author zinic
 */
public interface AtomEventHandlerRelay {

   void enlistHandler(AtomEventlet handler) throws InitializationException;

   void enlistHandler(AtomEventlet handler, Selector selector) throws InitializationException;

   void enlistHandlerContext(InstanceContext<AtomEventlet> handler) throws InitializationException;

   void enlistHandlerContext(InstanceContext<AtomEventlet> handler, Selector selector) throws InitializationException;
}
