package org.atomnuke.service;

/**
 *
 * @author zinic
 */
public interface Service extends ServiceLifeCycle {

   String name();

   Object instance();
}
