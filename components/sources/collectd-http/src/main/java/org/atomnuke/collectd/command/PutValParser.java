package org.atomnuke.collectd.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Sample: PUTVAL collect-n01.virt.xlocal/memory-fat/memory-cached interval=10
 * 1351213958:23736320.000000
 *
 * Regex:
 * PUTVAL\s([^/]+)/([^/-]+)(?:-([^\s]+))?/([^\-\s]+)(?:-([^\s]+))?\sinterval=([\d]+)\s([\d.]+):([\d.]+)
 *
 * Result: $1 [collect-n01.virt.xlocal] $2 [memory] $3 [fat] $4 [memory] $5
 * [cached] $6 [10] $7 [1351213958] $8 [23736320.000000]
 *
 * @author zinic
 */
public class PutValParser {

   private static final Pattern PUTVAL_COMMAND_PATTERN = Pattern.compile("PUTVAL\\s([^/]+)/([^/-]+)(?:-([^\\s]+))?/([^\\-\\s]+)(?:-([^\\s]+))?\\sinterval=([\\d]+)\\s([\\d.]+):([\\d.]+)");
   private static final int HOST = 1, PLUGIN = 2, PLUGIN_INSTANCE = 3, TYPE = 4, TYPE_INSTANCE = 5, INTERVAL = 6, TIMESTAMP = 7, VALUE = 8;

   private static final PutValParser INSTANCE = new PutValParser();

   public static PutValParser instance() {
      return INSTANCE;
   }

   private PutValParser() {
   }

   public PutValCommand parse(String line) {
      final Matcher matcher = PUTVAL_COMMAND_PATTERN.matcher(line);

      if (!matcher.matches()) {
         return null;
      }

      return new PutValCommand(matcher.group(HOST),
              matcher.group(PLUGIN),
              matcher.group(PLUGIN_INSTANCE),
              matcher.group(TYPE),
              matcher.group(TYPE_INSTANCE),
              matcher.group(INTERVAL),
              matcher.group(TIMESTAMP),
              matcher.group(VALUE));
   }
}
