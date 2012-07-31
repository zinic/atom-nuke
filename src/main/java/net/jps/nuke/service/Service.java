package net.jps.nuke.service;

/**
 *
 * @author zinic
 */
public interface Service {

   void init() throws InitializationException;

   void destroy() throws DestructionException;
}
