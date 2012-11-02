package org.atomnuke.sink.eps;

import org.atomnuke.sink.eps.eventlet.AtomEventlet;
import org.atomnuke.sink.eps.selector.EntrySelector;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface AtomEventletHandler {

   CancellationRemote enlistHandler(AtomEventlet handler);

   CancellationRemote enlistHandler(AtomEventlet handler, EntrySelector selector);

   CancellationRemote enlistHandler(InstanceContext<? extends AtomEventlet> handler);

   CancellationRemote enlistHandler(InstanceContext<? extends AtomEventlet> handler, EntrySelector selector);
}
