package org.atomnuke.source.result;

import org.atomnuke.source.action.AtomSourceAction;
import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;

/**
 *
 * @author zinic
 */
public interface AtomSourceResult {

   ResultType type();

   AtomSourceAction action();

   Feed feed();

   Entry entry();
}
