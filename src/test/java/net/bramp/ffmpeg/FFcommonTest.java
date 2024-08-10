package net.bramp.ffmpeg;

import net.bramp.ffmpeg.io.ProcessUtils;
import net.bramp.ffmpeg.lang.NewProcessAnswer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static net.bramp.ffmpeg.FFmpegTest.argThatHasItem;
import static net.bramp.ffmpeg.FFmpegTest.argThatNotHasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public abstract class FFcommonTest {
    private FFcommon asyncRunner;
    private ExecutorService executor;
    private ProcessFunction runFunc;
    private InputStream processOutputStream;
    private InputStream processErrorStream;
    private List<String> args;

    abstract FFcommon getFFcommon(ProcessFunction processFunction) throws IOException;

    @Before
    public void setUp() throws IOException {
        executor = Executors.newCachedThreadPool();
        runFunc = mock(RunProcessFunction.class);
        processOutputStream = new ByteArrayInputStream(new byte[32]);
        processErrorStream = new ByteArrayInputStream(new byte[32]);
        args = Collections.emptyList();

        lenient().when(runFunc.run(argThatHasItem("-version")))
                .thenAnswer(new NewProcessAnswer("ffmpeg-version"));

        asyncRunner = getFFcommon(runFunc);
    }

    @After
    public void tearDown() {
        executor.shutdown();
    }

    @Test
    public void testRunAsyncCompletesSuccessfully() throws Exception {
        Process mockProcess = mock(Process.class);
        when(runFunc.run(argThatNotHasItem("-version"))).thenReturn(mockProcess);
        when(mockProcess.getInputStream()).thenReturn(processOutputStream);
        when(mockProcess.getErrorStream()).thenReturn(processErrorStream);

        Future<Void> future = asyncRunner.runAsync(args);
        future.get();

        assertFalse(future.isCancelled());
        assertTrue(future.isDone());
        verify(mockProcess).destroy();
    }

    @Test
    public void testRunAsyncHandlesException() throws Exception {
        when(runFunc.run(any())).thenThrow(new IOException("Mock IOException"));

        Future<Void> future = asyncRunner.runAsync(args);

        assertThrows(Exception.class, future::get);
        assertTrue(future.isDone());
    }

    @Test
    public void testRunAsyncCancellation() throws Exception {
        Process mockProcess = mock(Process.class);
        when(mockProcess.getInputStream()).thenReturn(mock(InputStream.class));
        when(mockProcess.getErrorStream()).thenReturn(mock(InputStream.class));

        when(runFunc.run(any())).thenReturn(mockProcess);

        Future<Void> future = asyncRunner.runAsync(Collections.singletonList("arg"), executor);
        future.cancel(true);

        verify(mockProcess, timeout(1000)).destroy();
        assertTrue(future.isCancelled());

    }

    @Test
    public void testRunAsyncProcessTermination() throws Exception {
        Process mockProcess = mock(Process.class);
        when(runFunc.run(any())).thenReturn(mockProcess);
        when(mockProcess.getInputStream()).thenReturn(processOutputStream);
        when(mockProcess.getErrorStream()).thenReturn(processErrorStream);

        Future<Void> future = asyncRunner.runAsync(args);
        future.get();

        verify(mockProcess).destroy();
    }

    @Test
    public void testRunProcessWaitForThrowsInterruptedException() throws Exception {
        Process mockProcess = mock(Process.class);
        when(runFunc.run(any())).thenReturn(mockProcess);
        when(mockProcess.getInputStream()).thenReturn(processOutputStream);
        when(mockProcess.getErrorStream()).thenReturn(processErrorStream);
        when(mockProcess.waitFor(anyLong(), any(TimeUnit.class))).thenThrow(new InterruptedException());

        Future<Void> future = asyncRunner.runAsync(args);
        future.get();

        verify(mockProcess).destroy();
    }

    @Test
    public void testRunProcessWaitForThrowsTimeoutException() throws Exception {
        Process mockProcess = mock(Process.class);
        when(runFunc.run(any())).thenReturn(mockProcess);
        when(mockProcess.getInputStream()).thenReturn(processOutputStream);
        when(mockProcess.getErrorStream()).thenReturn(processErrorStream);
        when(mockProcess.isAlive()).thenReturn(true);

        Future<Void> future = asyncRunner.runAsync(args);
        ExecutionException executionException = assertThrows(ExecutionException.class, future::get);
        assertEquals(IOException.class, executionException.getCause().getClass());

        verify(mockProcess).destroy();
    }
}