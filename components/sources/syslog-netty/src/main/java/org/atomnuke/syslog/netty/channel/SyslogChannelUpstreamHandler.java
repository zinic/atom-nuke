package org.atomnuke.syslog.netty.channel;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class SyslogChannelUpstreamHandler extends SimpleChannelUpstreamHandler {

   private static final Logger LOG = LoggerFactory.getLogger(SyslogChannelUpstreamHandler.class);

   @Override
   public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
      final ChannelBuffer input = (ChannelBuffer) e.getMessage();

      if (input.readable()) {
      }
   }

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
      LOG.error(e.getCause().getMessage(), e.getCause());
   }
}
