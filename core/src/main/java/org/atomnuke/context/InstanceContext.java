package org.atomnuke.context;

/**
 *
 * @author zinic
 */
public interface InstanceContext<T> {

   T getInstance();

   void stepOut();

   void stepInto();
}
