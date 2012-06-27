package net.jps.nuke.atom.model;

import java.net.URI;
import net.jps.nuke.atom.model.annotation.Restriction;

/**
 *
 * @author zinic
 */
public interface XMLAttributes {

   URI base();

   @Restriction("[A-Za-z]{1,8}(-[A-Za-z0-9]{1,8})*")
   String lang();
}
