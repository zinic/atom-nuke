package org.atomnuke.task.context;

import org.atomnuke.context.Context;
import org.atomnuke.task.Tasker;
import org.slf4j.Logger;

/**
 *
 * @author zinic
 */
public interface AtomTaskContext extends Context {

   Tasker atomTasker();

   Logger log();
}
