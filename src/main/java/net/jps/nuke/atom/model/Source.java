package net.jps.nuke.atom.model;

import java.util.List;

/**
 *
 * @author zinic
 */
public interface Source extends AtomCommonAtributes {

   List<PersonConstruct> authors();

   List<Category> categories();

   Generator generator();

   Icon icon();

   ID id();

   List<Link> links();

   Logo logo();

   Rights rights();

   Subtitle subtitle();

   Title title();

   Updated updated();
}