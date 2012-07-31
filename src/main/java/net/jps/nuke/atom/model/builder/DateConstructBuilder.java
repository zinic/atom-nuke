package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.impl.DateConstructImpl;

/**
 *
 * @author zinic
 */
public class DateConstructBuilder extends DateConstructImpl {

   public DateConstructBuilder() {
      dateStringBuilder = new StringBuilder();
   }

   public StringBuilder getDateStringBuilder() {
      return dateStringBuilder;
   }

   public DateConstructBuilder setBase(URI base) {
      this.base = base;
      return this;
   }

   public DateConstructBuilder setLang(String lang) {
      this.lang = lang;
      return this;
   }
}
