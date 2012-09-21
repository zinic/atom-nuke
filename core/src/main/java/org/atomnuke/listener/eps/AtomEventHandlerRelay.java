package org.atomnuke.listener.eps;

import org.atomnuke.context.InstanceContext;
import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.listener.eps.selector.Selector;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface AtomEventHandlerRelay {

   CancellationRemote enlistHandler(AtomEventlet handler);

   CancellationRemote enlistHandler(AtomEventlet handler, Selector selector);

   CancellationRemote enlistHandlerContext(InstanceContext<? extends AtomEventlet> handler);

   CancellationRemote enlistHandlerContext(InstanceContext<? extends AtomEventlet> handler, Selector selector);
}
