package org.atomnuke.container.packaging.bindings;

import org.atomnuke.config.model.LanguageType;

/**
 *
 * @author zinic
 */
public interface LanguageDescriptor {

   String[] fileExtensions();

   LanguageType languageType();
}
