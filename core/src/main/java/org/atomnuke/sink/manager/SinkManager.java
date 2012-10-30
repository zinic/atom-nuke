package org.atomnuke.sink.manager;

import java.util.List;
import org.atomnuke.sink.AtomSink;
import org.atomnuke.plugin.InstanceContext;
import org.atomnuke.util.remote.CancellationRemote;

/**
 *
 * @author zinic
 */
public interface SinkManager {

   CancellationRemote addSink(InstanceContext<? extends AtomSink> atomSinkContext);

   boolean hasRegisteredSinks();

   List<ManagedSink> sinks();
}