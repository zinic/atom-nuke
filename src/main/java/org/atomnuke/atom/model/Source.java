package org.atomnuke.atom.model;

import java.util.List;

/**
 *
 * @author zinic
 */
public interface Source extends AtomCommonAtributes {

   List<Author> authors();

   List<Category> categories();

   Generator generator();

   Icon icon();

   Id id();

   List<Link> links();

   Logo logo();

   Rights rights();

   Subtitle subtitle();

   Title title();

   Updated updated();
}