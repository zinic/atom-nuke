package org.atomnuke.atom.model.builder;

/**
 *
 * @author zinic
 */
public interface ValueBuilder<T extends ValueBuilder> {

   T appendValue(String value);
}
