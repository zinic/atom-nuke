package org.atomnuke.listener;

import org.atomnuke.atom.model.Link;

/**
 *
 * @author zinic
 */
public interface ListenerResult {

    ListenerAction getAction();

    Link getLink();

    String getMessage();
}
