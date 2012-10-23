package org.atomnuke.task.context;

import org.atomnuke.context.Context;
import org.atomnuke.task.manager.AtomTasker;
import org.slf4j.Logger;

/**
 *
 * @author zinic
 */
public interface AtomTaskContext extends Context {

   AtomTasker atomTasker();

   Logger log();
}
