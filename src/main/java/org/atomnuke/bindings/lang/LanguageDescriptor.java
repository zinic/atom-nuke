package org.atomnuke.bindings.lang;

import org.atomnuke.config.model.LanguageType;

/**
 *
 * @author zinic
 */
public interface LanguageDescriptor {

   String[] fileExtensions();

   LanguageType languageType();
}
