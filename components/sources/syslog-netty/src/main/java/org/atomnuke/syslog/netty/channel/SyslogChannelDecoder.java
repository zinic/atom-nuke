package org.atomnuke.syslog.netty.channel;

import org.atomnuke.syslog.parser.FramingSyslogParser;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class SyslogChannelDecoder extends SimpleChannelUpstreamHandler {

   private static final Logger LOG = LoggerFactory.getLogger(SyslogChannelDecoder.class);
   
   private final FramingSyslogParser parser;

   public SyslogChannelDecoder() {
      parser = new FramingSyslogParser();
   }
   
   @Override
   public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
      final ChannelBuffer input = (ChannelBuffer) e.getMessage();

      if (input.readable()) {
         parser.next(input.readByte());
         
         switch (parser.getState()) {
            case ERROR:
               LOG.error("Error parsing message: " + parser.getErrorMessage());
               parser.reset();
               break;
               
            case STOP:
               Channels.fireMessageReceived(ctx, parser.getResult());
               break;
         }
      }
   }

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
      LOG.error(e.getCause().getMessage(), e.getCause());
   }
}
