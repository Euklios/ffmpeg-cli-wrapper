module ffmpeg_cli_wrapper {
  requires org.slf4j;
  requires jsr305;
  requires com.google.errorprone.annotations;
  requires com.github.spotbugs.annotations;
  requires org.apache.commons.lang3;
  requires com.google.common;
  requires java.desktop;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.annotation;
  requires com.fasterxml.jackson.databind;

  exports net.bramp.ffmpeg;
  exports net.bramp.ffmpeg.builder;
  exports net.bramp.ffmpeg.info;
  exports net.bramp.ffmpeg.io;
  exports net.bramp.ffmpeg.job;
  exports net.bramp.ffmpeg.nut;
  exports net.bramp.ffmpeg.options;
  exports net.bramp.ffmpeg.probe;
  exports net.bramp.ffmpeg.progress;
  exports net.bramp.ffmpeg.helper;

  opens net.bramp.ffmpeg.info to
      org.apache.commons.lang3;
  opens net.bramp.ffmpeg.options to
      org.apache.commons.lang3;
  opens net.bramp.ffmpeg.probe to
      org.apache.commons.lang3,
      com.fasterxml.jackson.databind;
  opens net.bramp.ffmpeg.builder to
      org.apache.commons.lang3;
}
