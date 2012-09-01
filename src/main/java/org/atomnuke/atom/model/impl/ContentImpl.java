package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Content;

/**
 *
 * @author zinic
 */
public class ContentImpl extends SimpleContentImpl implements Content {

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
}
