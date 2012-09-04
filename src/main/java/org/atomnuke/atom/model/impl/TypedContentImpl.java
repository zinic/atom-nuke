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
public class TypedContentImpl extends SimpleContentImpl implements Rights, Subtitle, Title, Summary {

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

   @Override
   public boolean equals(Object obj) {
      if (obj == null || getClass() != obj.getClass()) {
         return false;
      }

      final TypedContentImpl other = (TypedContentImpl) obj;

      if (this.type != other.type) {
         return false;
      }

      return super.equals(obj);
   }


}
