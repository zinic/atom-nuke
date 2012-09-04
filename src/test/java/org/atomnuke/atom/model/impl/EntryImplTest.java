package org.atomnuke.atom.model.impl;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.atomnuke.atom.model.ModelTestUtil.*;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.atom.model.builder.IdBuilder;

/**
 *
 * @author zinic
 */
@RunWith(Enclosed.class)
public class EntryImplTest {

   public static class WhenComparingEntries {

      @Test
      public void shouldIdentifyEqualEntries() {
         assertEquals(newEntry(), newEntry());
      }

      @Test
      public void shouldNonEqualEntries() {
         assertFalse(newEntry().equals(new EntryBuilder().setId(new IdBuilder().setValue("This is an entry id").build()).build()));
      }
   }
}
