package org.atomnuke.syslog.netty.channel;

import java.util.UUID;
import org.atomnuke.atom.model.builder.CategoryBuilder;
import org.atomnuke.atom.model.builder.ContentBuilder;
import org.atomnuke.atom.model.builder.EntryBuilder;
import org.atomnuke.atom.model.builder.IdBuilder;
import org.atomnuke.atom.model.builder.PublishedBuilder;
import org.atomnuke.fallout.source.queue.QueueSource;
import org.atomnuke.syslog.SyslogMessage;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zinic
 */
public class SyslogMessageHandler extends SimpleChannelHandler {

   private static final Logger LOG = LoggerFactory.getLogger(SyslogMessageHandler.class);
   private final QueueSource queueSource;

   public SyslogMessageHandler(QueueSource queueSource) {
      this.queueSource = queueSource;
   }

   @Override
   public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
      if (e.getMessage() instanceof SyslogMessage) {
         final SyslogMessage syslogMessage = (SyslogMessage) e.getMessage();
         final EntryBuilder atomEntryBuilder = new EntryBuilder();

         atomEntryBuilder.setId(new IdBuilder().setValue(UUID.randomUUID().toString()).build());
         atomEntryBuilder.setPublished(new PublishedBuilder().setValue(syslogMessage.timestamp()).build());
         atomEntryBuilder.addCategory(new CategoryBuilder().setScheme("app").setTerm("syslog").build());
         atomEntryBuilder.setContent(new ContentBuilder().setType("application/text").setValue(syslogMessage.content()).build());
         
         queueSource.put(atomEntryBuilder.build());
      }
   }

   @Override
   public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
      LOG.error(e.getCause().getMessage(), e.getCause());
   }
}
