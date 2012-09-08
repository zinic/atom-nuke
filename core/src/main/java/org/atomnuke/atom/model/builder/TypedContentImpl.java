package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Rights;
import org.atomnuke.atom.model.Subtitle;
import org.atomnuke.atom.model.Summary;
import org.atomnuke.atom.model.Title;
import org.atomnuke.atom.model.Type;

/**
 *
 * @author zinic
 */
class TypedContentImpl extends SimpleContentImpl implements Rights, Subtitle, Title, Summary {

   private Type type;

   public void setType(Type type) {
      this.type = type;
   }

   @Override
   public Type type() {
      return type;
   }

   @Override
   public int hashCode() {
      int hash = 7;

      hash = 59 * hash + (this.type != null ? this.type.hashCode() : 0);

      return hash + super.hashCode();
   }
}
