package net.jps.nuke.atom.model.impl;

import net.jps.nuke.atom.model.Content;

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
