package org.atomnuke.listener.eps;

import org.atomnuke.plugin.InstanceEnvironment;
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

   CancellationRemote enlistHandlerContext(InstanceEnvironment<? extends AtomEventlet> handler);

   CancellationRemote enlistHandlerContext(InstanceEnvironment<? extends AtomEventlet> handler, Selector selector);
}
