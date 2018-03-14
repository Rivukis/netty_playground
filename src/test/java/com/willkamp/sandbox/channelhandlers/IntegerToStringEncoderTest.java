package com.willkamp.sandbox.channelhandlers;

import com.willkamp.sandbox.channelhandlers.IntegerToStringEncoder;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class IntegerToStringEncoderTest {
  @Test
  public void testEncoded() {
    Integer testValue = 9000;

    EmbeddedChannel channel = new EmbeddedChannel(new IntegerToStringEncoder());
    assertTrue(channel.writeOutbound(testValue));
    assertTrue(channel.finish());

    assertEquals(String.valueOf(testValue), channel.readOutbound());
  }
}
