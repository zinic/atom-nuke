package org.atomnuke.util;

import org.atomnuke.config.model.LanguageType;
import org.atomnuke.container.packaging.bindings.lang.BindingLanguage;

/**
 *
 * @author zinic
 */
public final class LanguageTypeUtil {

   private LanguageTypeUtil() {
   }

   public static BindingLanguage asBindingLanguage(LanguageType lt) {
      switch (lt) {
         case JAVA:
            return BindingLanguage.JAVA;

         case JAVASCRIPT:
            return BindingLanguage.JAVASCRIPT;

         case PYTHON:
            return BindingLanguage.PYTHON;

         default:
            return null;
      }
   }

   public static LanguageType asLanguageType(BindingLanguage bl) {
      switch (bl) {
         case JAVA:
            return LanguageType.JAVA;

         case JAVASCRIPT:
            return LanguageType.JAVASCRIPT;

         case PYTHON:
            return LanguageType.PYTHON;

         default:
            return null;
      }
   }
}
