package org.atomnuke.syslog.parser;

import java.nio.charset.Charset;
import org.jboss.netty.buffer.ChannelBuffer;

import static org.jboss.netty.buffer.ChannelBuffers.*;

/**
 *
 * @author zinic
 */
public class Accumulator {

   private final ChannelBuffer accumulatorBuffer;
   private int accumulatorLocation;

   public Accumulator() {
      this(256);
   }
   
   public Accumulator(int initialSize) {
      accumulatorBuffer = dynamicBuffer(initialSize);
   }

   public int size() {
      return accumulatorLocation;
   }

   public void add(byte c) {
      accumulatorBuffer.writeByte(c);
   }

   public String getAll(Charset charset) {
      return accumulatorBuffer.toString(charset);
   }
}
