package org.atomnuke.atom.model.builder;

import org.atomnuke.atom.model.Content;

/**
 *
 * @author zinic
 */
class ContentImpl extends SimpleContentImpl implements Content {

   private String type, src;

   public void setType(String type) {
      this.type = type;
   }

   public void setSrc(String src) {
      this.src = src;
   }

   @Override
   public String type() {
      return type;
   }

   @Override
   public String src() {
      return src;
   }

   @Override
   public int hashCode() {
      int hash = 3;

      hash = 97 * hash + (this.type != null ? this.type.hashCode() : 0);
      hash = 97 * hash + (this.src != null ? this.src.hashCode() : 0);

      return hash + super.hashCode();
   }
}
