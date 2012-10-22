package org.atomnuke.listener.eps;

import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.listener.eps.selector.Selector;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface AtomEventletHandler {

   CancellationRemote enlistHandler(AtomEventlet handler);

   CancellationRemote enlistHandler(AtomEventlet handler, Selector selector);

   CancellationRemote enlistHandler(InstanceContext<? extends AtomEventlet> handler);

   CancellationRemote enlistHandler(InstanceContext<? extends AtomEventlet> handler, Selector selector);
}
