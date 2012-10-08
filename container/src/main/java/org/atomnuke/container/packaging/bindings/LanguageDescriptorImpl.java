package org.atomnuke.container.packaging.bindings;

import org.atomnuke.config.model.LanguageType;

/**
 *
 * @author zinic
 */
public class LanguageDescriptorImpl implements LanguageDescriptor {

   private static final String[] EMPTY_STRING_ARRAY = new String[0];

   private final LanguageType languageType;
   private final String[] fileExtensions;

   public LanguageDescriptorImpl(LanguageType languageType) {
      this(languageType, EMPTY_STRING_ARRAY);
   }

   public LanguageDescriptorImpl(LanguageType languageType, String... fileExtensions) {
      this.languageType = languageType;
      this.fileExtensions = fileExtensions;
   }

   @Override
   public String[] fileExtensions() {
      return fileExtensions;
   }

   @Override
   public LanguageType languageType() {
      return languageType;
   }
}
