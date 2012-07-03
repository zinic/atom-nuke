package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.Published;
import net.jps.nuke.atom.model.Updated;
import net.jps.nuke.atom.model.impl.DateConstructImpl;

/**
 *
 * @author zinic
 */
public class DateConstructBuilder extends DateConstructImpl {

   public static DateConstructBuilder newBuilder() {
      return new DateConstructBuilder();
   }

   protected DateConstructBuilder() {
      dateStringBuilder = new StringBuilder();
   }

   public Published buildPublished() {
      return this;
   }

   public Updated buildUpdated() {
      return this;
   }

   public StringBuilder getDateStringBuilder() {
      return dateStringBuilder;
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
