package org.atomnuke.atom.model.builder;

import java.net.URI;
import org.atomnuke.atom.model.AtomCommonAtributes;

/**
 *
 * @author zinic
 */
public interface AtomConstructBuilder<T extends AtomConstructBuilder, B extends AtomCommonAtributes> {

   B build();

   T setBase(URI base);

   T setLang(String lang);
}
