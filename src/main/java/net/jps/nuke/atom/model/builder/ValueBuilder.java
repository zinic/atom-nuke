package net.jps.nuke.atom.model.builder;

/**
 *
 * @author zinic
 */
public interface ValueBuilder<T extends ValueBuilder> {

   public T setValue(String value);

   public T appendValue(String value);
}
