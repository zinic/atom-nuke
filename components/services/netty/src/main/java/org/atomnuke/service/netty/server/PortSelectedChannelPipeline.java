package org.atomnuke.service.netty.server;

import java.util.HashMap;
import java.util.Map;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;

/**
 *
 * @author zinic
 */
public class PortSelectedChannelPipeline implements ChannelPipelineFactory {

   private final Map<Integer, ChannelPipelineFactory> pipelines;

   public PortSelectedChannelPipeline() {
      pipelines = new HashMap<Integer, ChannelPipelineFactory>();
   }
   
   @Override
   public ChannelPipeline getPipeline() throws Exception {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
