package org.atomnuke.syslog.parser;

import java.nio.charset.Charset;
import org.jboss.netty.buffer.ChannelBuffer;

import static org.jboss.netty.buffer.ChannelBuffers.*;

/**
 *
 * @author zinic
 */
public class Accumulator {

   private ChannelBuffer frontBuffer, backBuffer;
   private int accumulatorLocation;

   public Accumulator() {
      this(256);
   }
   
   public Accumulator(int initialSize) {
      frontBuffer = dynamicBuffer(initialSize);
      backBuffer = directBuffer(initialSize);
   }
   
   public void switchBuffers() {
      final ChannelBuffer currentFront = frontBuffer;
      
      frontBuffer = backBuffer;
      backBuffer = currentFront;
   }

   public int size() {
      return frontBuffer.readableBytes();
   }

   public void add(byte c) {
      frontBuffer.writeByte(c);
   }

   public String getAll(Charset charset) {
      final String value = frontBuffer.toString(charset);
      frontBuffer.clear();
      
      return value;
   }
}
