package org.atomnuke.atom.model.impl;

import org.atomnuke.atom.model.Content;

/**
 *
 * @author zinic
 */
public abstract class ContentImpl extends LangAwareTextElement implements Content {

   protected String type;
   protected String src;

   public String type() {
      return type;
   }

   public String src() {
      return src;
   }
}
