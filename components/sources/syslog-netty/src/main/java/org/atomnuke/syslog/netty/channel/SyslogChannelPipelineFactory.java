package org.atomnuke.syslog.netty.channel;

import org.atomnuke.util.source.QueueSource;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

/**
 *
 * @author zinic
 */
public class SyslogChannelPipelineFactory implements ChannelPipelineFactory {

   private final QueueSource messagePipe;

   public SyslogChannelPipelineFactory(QueueSource messagePipe) {
      this.messagePipe = messagePipe;
   }
   
   @Override
   public ChannelPipeline getPipeline() throws Exception {
      return Channels.pipeline(new SyslogChannelDecoder(), new SyslogMessageHandler(messagePipe));
   }
}
