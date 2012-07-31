package net.jps.nuke.atom.model.impl;

import net.jps.nuke.atom.model.Id;
import net.jps.nuke.atom.model.Icon;
import net.jps.nuke.atom.model.Logo;

/**
 *
 * @author zinic
 */
public abstract class LangAwareTextElement extends AtomCommonAttributesImpl implements Id, Icon, Logo {

   protected StringBuilder value;

   public String value() {
      return value.toString();
   }
}
