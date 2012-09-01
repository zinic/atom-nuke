package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Rights;
import org.atomnuke.atom.model.Subtitle;
import org.atomnuke.atom.model.Summary;
import org.atomnuke.atom.model.Title;
import org.atomnuke.atom.model.Type;

/**
 *
 * @author zinic
 */
public class TypedContentImpl extends SimpleContent implements Rights, Subtitle, Title, Summary {

   private Type type;

   public void setType(Type type) {
      this.type = type;
   }

   @Override
   public Type type() {
      return type;
   }
}
