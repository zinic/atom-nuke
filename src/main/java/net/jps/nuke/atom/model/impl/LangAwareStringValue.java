package net.jps.nuke.atom.model.impl;

import net.jps.nuke.atom.model.ID;
import net.jps.nuke.atom.model.Icon;
import net.jps.nuke.atom.model.Logo;

/**
 *
 * @author zinic
 */
public abstract class LangAwareStringValue extends AtomCommonAttributesImpl implements ID, Icon, Logo {

   protected StringBuilder value;

   public String value() {
      return value.toString();
   }
}
