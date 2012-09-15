package org.atomnuke.listener.eps;

import org.atomnuke.context.InstanceContext;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.listener.eps.selector.Selector;
import org.atomnuke.task.lifecycle.InitializationException;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface AtomEventHandlerRelay {

   CancellationRemote enlistHandler(AtomEventlet handler) throws InitializationException;

   CancellationRemote enlistHandler(AtomEventlet handler, Selector selector) throws InitializationException;

   CancellationRemote enlistHandlerContext(InstanceContext<? extends AtomEventlet> handler) throws InitializationException;

   CancellationRemote enlistHandlerContext(InstanceContext<? extends AtomEventlet> handler, Selector selector) throws InitializationException;
}
