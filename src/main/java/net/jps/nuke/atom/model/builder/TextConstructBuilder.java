package net.jps.nuke.atom.model.builder;

import java.net.URI;
import net.jps.nuke.atom.model.Rights;
import net.jps.nuke.atom.model.Subtitle;
import net.jps.nuke.atom.model.Summary;
import net.jps.nuke.atom.model.Title;
import net.jps.nuke.atom.model.Type;
import net.jps.nuke.atom.model.impl.TextConstructImpl;

/**
 *
 * @author zinic
 */
public class TextConstructBuilder extends TextConstructImpl {

   public static TextConstructBuilder newBuilder() {
      return new TextConstructBuilder();
   }

   protected TextConstructBuilder() {
      this.value = new StringBuilder();
   }

   public Rights buildRights() {
      return this;
   }

   public Subtitle buildSubtitle() {
      return this;
   }

   public Title buildTitle() {
      return this;
   }

   public Summary buildSummary() {
      return this;
   }

   public void setType(Type type) {
      this.type = type;
   }

   public StringBuilder getValueBuilder() {
      return value;
   }

   public void setBase(URI base) {
      this.base = base;
   }

   public void setLang(String lang) {
      this.lang = lang;
   }
}
