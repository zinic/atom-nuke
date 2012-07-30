package net.jps.nuke.listener;

import net.jps.nuke.atom.model.Link;

/**
 *
 * @author zinic
 */
public interface ListenerResult {

    ListenerAction getAction();

    Link getLink();

    String getMessage();
}
