package org.atomnuke.listener.eps;

import org.atomnuke.listener.eps.eventlet.AtomEventlet;
import org.atomnuke.listener.eps.selector.Selector;
import org.atomnuke.plugin.Environment;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface AtomEventHandlerRelay {

   CancellationRemote enlistHandler(AtomEventlet handler);

   CancellationRemote enlistHandler(AtomEventlet handler, Selector selector);

   CancellationRemote enlistHandlerContext(Environment environment, AtomEventlet handler);

   CancellationRemote enlistHandlerContext(Environment environment, AtomEventlet handler, Selector selector);
}
