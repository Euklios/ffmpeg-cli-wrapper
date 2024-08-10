package net.bramp.ffmpeg;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.collect.Lists;
import net.bramp.ffmpeg.fixtures.Codecs;
import net.bramp.ffmpeg.fixtures.Filters;
import net.bramp.ffmpeg.fixtures.Formats;
import net.bramp.ffmpeg.fixtures.ChannelLayouts;
import net.bramp.ffmpeg.fixtures.PixelFormats;
import net.bramp.ffmpeg.info.Filter;
import net.bramp.ffmpeg.lang.NewProcessAnswer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FFmpegTest extends FFcommonTest {

  @Mock ProcessFunction runFunc;

  FFmpeg ffmpeg;

  @Before
  public void before() throws IOException {
    when(runFunc.run(argThatHasItem("-version")))
        .thenAnswer(new NewProcessAnswer("ffmpeg-version"));
    when(runFunc.run(argThatHasItem("-formats")))
        .thenAnswer(new NewProcessAnswer("ffmpeg-formats"));
    when(runFunc.run(argThatHasItem("-codecs"))).thenAnswer(new NewProcessAnswer("ffmpeg-codecs"));
    when(runFunc.run(argThatHasItem("-pix_fmts")))
        .thenAnswer(new NewProcessAnswer("ffmpeg-pix_fmts"));
    when(runFunc.run(argThatHasItem("toto.mp4")))
        .thenAnswer(new NewProcessAnswer("ffmpeg-version", "ffmpeg-no-such-file"));
    when(runFunc.run(argThatHasItem("-filters")))
        .thenAnswer(new NewProcessAnswer("ffmpeg-filters"));
    when(runFunc.run(argThatHasItem("-layouts")))
            .thenAnswer(new NewProcessAnswer("ffmpeg-layouts"));

    ffmpeg = new FFmpeg(runFunc);
  }

  @SuppressWarnings("unchecked")
  public static <T> List<T> argThatHasItem(T s) {
    return (List<T>) argThat(hasItem(s));
  }

  public static <T> List<T> argThatNotHasItem(T s) {
    return AdditionalMatchers.not(argThatHasItem(s));
  }

  @Test
  public void testVersion() throws Exception {
    assertEquals("ffmpeg version 0.10.9-7:0.10.9-1~raring1", ffmpeg.version());
    assertEquals("ffmpeg version 0.10.9-7:0.10.9-1~raring1", ffmpeg.version());

    verify(runFunc, times(1)).run(argThatHasItem("-version"));
  }

  @Test
  public void testCodecs() throws IOException {
    // Run twice, the second should be cached
    assertEquals(Codecs.CODECS, ffmpeg.codecs());
    assertEquals(Codecs.CODECS, ffmpeg.codecs());

    verify(runFunc, times(1)).run(argThatHasItem("-codecs"));
  }

  @Test
  public void testFormats() throws IOException {
    // Run twice, the second should be cached
    assertEquals(Formats.FORMATS, ffmpeg.formats());
    assertEquals(Formats.FORMATS, ffmpeg.formats());

    verify(runFunc, times(1)).run(argThatHasItem("-formats"));
  }

  @Test
  public void testReadProcessStreams() throws IOException {
    Appendable processInputStream = mock(Appendable.class);
    ffmpeg.setProcessOutputStream(processInputStream);

    Appendable processErrStream = mock(Appendable.class);
    ffmpeg.setProcessErrorStream(processErrStream);

    ffmpeg.run(Lists.newArrayList("-i", "toto.mp4"));

    verify(processInputStream, times(1)).append(any(CharSequence.class));
    verify(processErrStream, times(1)).append(any(CharSequence.class));
  }

  @Test
  public void testReadProcessStreamsAsync() throws IOException, ExecutionException, InterruptedException {
    Appendable processInputStream = mock(Appendable.class);
    ffmpeg.setProcessOutputStream(processInputStream);

    Appendable processErrStream = mock(Appendable.class);
    ffmpeg.setProcessErrorStream(processErrStream);

    ffmpeg.runAsync(Lists.newArrayList("-i", "toto.mp4")).get();

    verify(processInputStream, times(1)).append(any(CharSequence.class));
    verify(processErrStream, times(1)).append(any(CharSequence.class));
  }

  @Test
  public void testPixelFormat() throws IOException {
    // Run twice, the second should be cached
    assertEquals(PixelFormats.PIXEL_FORMATS, ffmpeg.pixelFormats());
    assertEquals(PixelFormats.PIXEL_FORMATS, ffmpeg.pixelFormats());

    verify(runFunc, times(1)).run(argThatHasItem("-pix_fmts"));
  }

  @Test
  public void testFilters() throws IOException {
    // Run twice, the second should be cached

    List<Filter> filters = ffmpeg.filters();

    for (int i = 0; i < filters.size(); i++) {
      assertEquals(Filters.FILTERS.get(i), filters.get(i));
    }

    assertEquals(Filters.FILTERS, ffmpeg.filters());
    assertEquals(Filters.FILTERS, ffmpeg.filters());

    verify(runFunc, times(1)).run(argThatHasItem("-filters"));
  }

  @Test
  public void testLayouts() throws IOException {
    assertEquals(ChannelLayouts.CHANNEL_LAYOUTS, ffmpeg.channelLayouts());
    assertEquals(ChannelLayouts.CHANNEL_LAYOUTS, ffmpeg.channelLayouts());

    verify(runFunc, times(1)).run(argThatHasItem("-layouts"));
  }

  @Override
  FFcommon getFFcommon(ProcessFunction runFunc) throws IOException {
    return new FFmpeg(runFunc);
  }
}
