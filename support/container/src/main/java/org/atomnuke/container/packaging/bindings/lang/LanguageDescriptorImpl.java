package org.atomnuke.container.packaging.bindings.lang;

/**
 *
 * @author zinic
 */
public class LanguageDescriptorImpl implements LanguageDescriptor {

   private static final String[] EMPTY_STRING_ARRAY = new String[0];

   private final BindingLanguage language;
   private final String[] fileExtensions;

   public LanguageDescriptorImpl(BindingLanguage language) {
      this(language, EMPTY_STRING_ARRAY);
   }

   public LanguageDescriptorImpl(BindingLanguage language, String... fileExtensions) {
      this.language = language;
      this.fileExtensions = fileExtensions;
   }

   @Override
   public String[] fileExtensions() {
      return fileExtensions;
   }

   @Override
   public BindingLanguage language() {
      return language;
   }
}
