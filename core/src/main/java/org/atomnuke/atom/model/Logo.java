package org.atomnuke.atom.model;

import org.atomnuke.atom.model.annotation.ContentConstraint;
import org.atomnuke.atom.model.constraint.AtomUriConstraint;

/**
 *
 * @author zinic
 */
@ContentConstraint(AtomUriConstraint.class)
public interface Logo extends SimpleContent {
}
