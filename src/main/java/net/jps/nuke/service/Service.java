package net.jps.nuke.service;

/**
 *
 * @author zinic
 */
public interface Service {

   void init() throws ServiceInitializationException;

   void destroy() throws ServiceDestructionException;
}
