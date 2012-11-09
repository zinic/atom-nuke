package org.atomnuke.sink;

import org.atomnuke.atom.model.Entry;
import org.atomnuke.atom.model.Feed;
import org.atomnuke.lifecycle.ResourceLifeCycle;
import org.atomnuke.task.context.AtomTaskContext;

/**
 *
 * @author zinic
 */
public interface AtomSink extends ResourceLifeCycle<AtomTaskContext> {

   SinkResult entry(Entry entry) throws AtomSinkException;

   SinkResult feedPage(Feed page) throws AtomSinkException;
}
