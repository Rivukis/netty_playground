package com.willkamp.sandbox.channelhandlers;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

public class IntegerToStringEncoderTest extends Assert {
  @Test
  public void testEncoded() {
    Integer testValue = 9000;

    EmbeddedChannel channel = new EmbeddedChannel(new IntegerToStringEncoder());
    assertTrue(channel.writeOutbound(testValue));
    assertTrue(channel.finish());

    assertEquals(String.valueOf(testValue), channel.readOutbound());
  }
}
