package org.atomnuke.container.service.construct;

/**
 *
 * @author zinic
 */
public interface Constructor {

  <T> T constructInstance(Class<T> instanceClass) throws ConstructionException;
}
