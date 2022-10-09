package net.bramp.ffmpeg.fixtures;

import com.google.common.collect.ImmutableList;
import net.bramp.ffmpeg.info.Protocol;

public class Protocols {
  private Protocols() {
    throw new AssertionError("No instances for you!");
  }

  public static final ImmutableList<Protocol> PROTOCOLS =
      new ImmutableList.Builder<Protocol>()
          .add(
              new Protocol("async", Protocol.ProtocolDirection.INPUT),
              new Protocol("bluray", Protocol.ProtocolDirection.INPUT),
              new Protocol("cache", Protocol.ProtocolDirection.INPUT),
              new Protocol("concat", Protocol.ProtocolDirection.INPUT),
              new Protocol("crypto", Protocol.ProtocolDirection.INPUT),
              new Protocol("data", Protocol.ProtocolDirection.INPUT),
              new Protocol("ffrtmpcrypt", Protocol.ProtocolDirection.INPUT),
              new Protocol("ffrtmphttp", Protocol.ProtocolDirection.INPUT),
              new Protocol("file", Protocol.ProtocolDirection.INPUT),
              new Protocol("ftp", Protocol.ProtocolDirection.INPUT),
              new Protocol("gopher", Protocol.ProtocolDirection.INPUT),
              new Protocol("gophers", Protocol.ProtocolDirection.INPUT),
              new Protocol("hls", Protocol.ProtocolDirection.INPUT),
              new Protocol("http", Protocol.ProtocolDirection.INPUT),
              new Protocol("httpproxy", Protocol.ProtocolDirection.INPUT),
              new Protocol("https", Protocol.ProtocolDirection.INPUT),
              new Protocol("mmsh", Protocol.ProtocolDirection.INPUT),
              new Protocol("mmst", Protocol.ProtocolDirection.INPUT),
              new Protocol("pipe", Protocol.ProtocolDirection.INPUT),
              new Protocol("rtmp", Protocol.ProtocolDirection.INPUT),
              new Protocol("rtmpe", Protocol.ProtocolDirection.INPUT),
              new Protocol("rtmps", Protocol.ProtocolDirection.INPUT),
              new Protocol("rtmpt", Protocol.ProtocolDirection.INPUT),
              new Protocol("rtmpte", Protocol.ProtocolDirection.INPUT),
              new Protocol("rtmpts", Protocol.ProtocolDirection.INPUT),
              new Protocol("rtp", Protocol.ProtocolDirection.INPUT),
              new Protocol("srtp", Protocol.ProtocolDirection.INPUT),
              new Protocol("subfile", Protocol.ProtocolDirection.INPUT),
              new Protocol("tcp", Protocol.ProtocolDirection.INPUT),
              new Protocol("tls", Protocol.ProtocolDirection.INPUT),
              new Protocol("udp", Protocol.ProtocolDirection.INPUT),
              new Protocol("udplite", Protocol.ProtocolDirection.INPUT),
              new Protocol("rist", Protocol.ProtocolDirection.INPUT),
              new Protocol("srt", Protocol.ProtocolDirection.INPUT),
              new Protocol("sftp", Protocol.ProtocolDirection.INPUT),
              new Protocol("zmq", Protocol.ProtocolDirection.INPUT),
              new Protocol("crypto", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("ffrtmpcrypt", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("ffrtmphttp", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("file", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("ftp", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("gopher", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("gophers", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("http", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("httpproxy", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("https", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("icecast", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("md5", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("pipe", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("prompeg", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("rtmp", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("rtmpe", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("rtmps", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("rtmpt", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("rtmpte", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("rtmpts", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("rtp", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("srtp", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("tee", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("tcp", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("tls", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("udp", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("udplite", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("rist", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("srt", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("sftp", Protocol.ProtocolDirection.OUTPUT),
              new Protocol("zmq", Protocol.ProtocolDirection.OUTPUT)
          )
          .build();
}
