package net.bramp.ffmpeg;

import static net.bramp.ffmpeg.FFmpegTest.argThatHasItem;
import static net.bramp.ffmpeg.FFmpegTest.argThatIsInstanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import net.bramp.ffmpeg.builder.ProcessOptions;
import net.bramp.ffmpeg.fixtures.Samples;
import net.bramp.ffmpeg.lang.NewProcessAnswer;
import net.bramp.ffmpeg.probe.*;
import org.apache.commons.lang3.math.Fraction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FFprobeTest {

  @Mock ProcessFunction runFunc;

  FFprobe ffprobe;

  @BeforeEach
  public void before() throws IOException {
    lenient()
        .when(runFunc.run(argThatHasItem("-version"), argThatIsInstanceOf(ProcessOptions.class)))
        .thenAnswer(new NewProcessAnswer("ffprobe-version"));

    lenient()
        .when(
            runFunc.run(
                argThatHasItem(Samples.big_buck_bunny_720p_1mb),
                argThatIsInstanceOf(ProcessOptions.class)))
        .thenAnswer(new NewProcessAnswer("ffprobe-big_buck_bunny_720p_1mb.mp4"));

    lenient()
        .when(
            runFunc.run(
                argThatHasItem(Samples.always_on_my_mind),
                argThatIsInstanceOf(ProcessOptions.class)))
        .thenAnswer(new NewProcessAnswer("ffprobe-Always On My Mind [Program Only] - Adelén.mp4"));

    lenient()
        .when(
            runFunc.run(
                argThatHasItem(Samples.start_pts_test), argThatIsInstanceOf(ProcessOptions.class)))
        .thenAnswer(new NewProcessAnswer("ffprobe-start_pts_test"));

    lenient()
        .when(
            runFunc.run(
                argThatHasItem(Samples.divide_by_zero), argThatIsInstanceOf(ProcessOptions.class)))
        .thenAnswer(new NewProcessAnswer("ffprobe-divide-by-zero"));

    lenient()
        .when(
            runFunc.run(
                argThatHasItem(Samples.book_with_chapters),
                argThatIsInstanceOf(ProcessOptions.class)))
        .thenAnswer(new NewProcessAnswer("book_with_chapters.m4b"));

    lenient()
        .when(
            runFunc.run(
                argThatHasItem(Samples.side_data_list), argThatIsInstanceOf(ProcessOptions.class)))
        .thenAnswer(new NewProcessAnswer("ffprobe-side_data_list"));

    ffprobe = new FFprobe(runFunc);
  }

  @Test
  public void testVersion() throws Exception {
    assertEquals(
        "ffprobe version 3.0.2 Copyright (c) 2007-2016 the FFmpeg developers", ffprobe.version());
    assertEquals(
        "ffprobe version 3.0.2 Copyright (c) 2007-2016 the FFmpeg developers", ffprobe.version());

    verify(runFunc, times(1))
        .run(argThatHasItem("-version"), argThatIsInstanceOf(ProcessOptions.class));
  }

  @Test
  public void testProbeVideo() throws IOException {
    FFmpegProbeResult info = ffprobe.probe(Samples.big_buck_bunny_720p_1mb);
    assertFalse(info.hasError());

    // Only a quick sanity check until we do something better
    assertThat(info.getStreams(), hasSize(2));
    assertThat(info.getStreams().get(0).codecType(), is(FFmpegStreamCodecType.VIDEO));
    assertThat(info.getStreams().get(1).codecType(), is(FFmpegStreamCodecType.AUDIO));

    assertThat(info.getStreams().get(1).channels(), is(6));
    assertThat(info.getStreams().get(1).sampleRate(), is(48_000));

    assertThat(info.getChapters().isEmpty(), is(true));
    // System.out.println(FFmpegUtils.getGson().toJson(info));
  }

  @Test
  public void testProbeBookWithChapters() throws IOException {
    FFmpegProbeResult info = ffprobe.probe(Samples.book_with_chapters);
    assertThat(info.hasError(), is(false));
    assertThat(info.getChapters().size(), is(24));

    FFmpegChapter firstChapter = info.getChapters().get(0);
    assertThat(firstChapter.timeBase(), is("1/44100"));
    assertThat(firstChapter.start(), is(0L));
    assertThat(firstChapter.startTime(), is("0.000000"));
    assertThat(firstChapter.end(), is(11951309L));
    assertThat(firstChapter.endTime(), is("271.004739"));
    assertThat(firstChapter.tags().title(), is("01 - Sammy Jay Makes a Fuss"));

    FFmpegChapter lastChapter = info.getChapters().get(info.getChapters().size() - 1);
    assertThat(lastChapter.timeBase(), is("1/44100"));
    assertThat(lastChapter.start(), is(237875790L));
    assertThat(lastChapter.startTime(), is("5394.008844"));
    assertThat(lastChapter.end(), is(248628224L));
    assertThat(lastChapter.endTime(), is("5637.828209"));
    assertThat(lastChapter.tags().title(), is("24 - Chatterer Has His Turn to Laugh"));
  }

  @Test
  public void testProbeVideo2() throws IOException {
    FFmpegProbeResult info = ffprobe.probe(Samples.always_on_my_mind);
    assertFalse(info.hasError());

    // Only a quick sanity check until we do something better
    assertThat(info.getStreams(), hasSize(2));
    assertThat(info.getStreams().get(0).codecType(), is(FFmpegStreamCodecType.VIDEO));
    assertThat(info.getStreams().get(1).codecType(), is(FFmpegStreamCodecType.AUDIO));

    assertThat(info.getStreams().get(1).channels(), is(2));
    assertThat(info.getStreams().get(1).sampleRate(), is(48_000));

    // Test a UTF-8 name
    assertThat(
        info.getFormat().filename(),
        is("c:\\Users\\Bob\\Always On My Mind [Program Only] - Adelén.mp4"));

    // System.out.println(FFmpegUtils.getGson().toJson(info));
  }

  @Test
  public void testProbeStartPts() throws IOException {
    FFmpegProbeResult info = ffprobe.probe(Samples.start_pts_test);
    assertFalse(info.hasError());

    // Check edge case with a time larger than an integer
    assertThat(info.getStreams().get(0).startPts(), is(8570867078L));
  }

  @Test
  public void testProbeDivideByZero() throws IOException {
    // https://github.com/bramp/ffmpeg-cli-wrapper/issues/10
    FFmpegProbeResult info = ffprobe.probe(Samples.divide_by_zero);
    assertFalse(info.hasError());

    assertThat(info.getStreams().get(1).codecTimeBase(), is(Fraction.ZERO));

    // System.out.println(FFmpegUtils.getGson().toJson(info));
  }

  @Test
  public void testProbeSideDataList() throws IOException {
    FFmpegProbeResult info = ffprobe.probe(Samples.side_data_list);

    // Check edge case with a time larger than an integer
    assertThat(info.getStreams().get(0).sideData().size(), is(1));
    assertThat(info.getStreams().get(0).sideData().get(0).sideDataType(), is("Display Matrix"));
    assertThat(
        info.getStreams().get(0).sideData().get(0).displaymatrix(),
        is(
            "\n00000000:            0      -65536           0\n00000001:        65536           0           0\n00000002:            0           0  1073741824\n"));
    assertThat(info.getStreams().get(0).sideData().get(0).rotation(), is(90));
  }

  @Test
  public void chaptersCannotBeNull() throws JsonProcessingException {
    FFmpegProbeResult result =
        FFmpegUtils.getObjectMapper().readValue("{}", FFmpegProbeResult.class);

    assertThat(result.chapters(), is(not(nullValue())));
  }

  @Test
  public void streamsCannotBeNull() throws JsonProcessingException {
    FFmpegProbeResult result =
        FFmpegUtils.getObjectMapper().readValue("{}", FFmpegProbeResult.class);

    assertThat(result.streams(), is(not(nullValue())));
  }

  @Test
  public void sideDataCannotBeNull() throws JsonProcessingException {
    FFmpegStream result = FFmpegUtils.getObjectMapper().readValue("{}", FFmpegStream.class);

    assertThat(result.sideData(), is(not(nullValue())));
  }

  @Test
  public void streamTagsCannotBeNull() throws JsonProcessingException {
    FFmpegStream result = FFmpegUtils.getObjectMapper().readValue("{}", FFmpegStream.class);

    assertThat(result.tags(), is(not(nullValue())));
  }

  @Test
  public void formatTagsCannotBeNull() throws JsonProcessingException {
    FFmpegFormat result = FFmpegUtils.getObjectMapper().readValue("{}", FFmpegFormat.class);

    assertThat(result.tags(), is(not(nullValue())));
  }
}
