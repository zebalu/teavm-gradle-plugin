package io.github.zebalu.gradle.teavm;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

@ExtendWith(MockitoExtension.class)
public class Slf4JTeavmLogTest {

    private static final String MESSAGE = "the test message";
    private static final Throwable THROWABLE = new IllegalStateException("Test error");
    
    @Mock
    private Logger mockLogger;

    @InjectMocks
    private Slf4JTeavmLog teaLog;

    @Test
    public void canBeCalledWithClass() {
        assertDoesNotThrow(() -> new Slf4JTeavmLog(Slf4JTeavmLogTest.class));
    }
    
    @Test
    public void debugIsForwarded() {
        teaLog.debug(MESSAGE);
        verify(mockLogger).debug(MESSAGE);
    }
    
    @Test
    public void infoIsForwarded() {
        teaLog.info(MESSAGE);
        verify(mockLogger).info(MESSAGE);
    }
    
    @Test
    public void warningIsForwarded() {
        teaLog.warning(MESSAGE);
        verify(mockLogger).warn(MESSAGE);
    }
    
    @Test
    public void errorIsForwarded() {
        teaLog.error(MESSAGE);
        verify(mockLogger).error(MESSAGE);
    }

    @Test
    public void debugErrorIsForwarded() {
        teaLog.debug(MESSAGE, THROWABLE);
        verify(mockLogger).debug(MESSAGE, THROWABLE);
    }
    
    @Test
    public void infoErrorIsForwarded() {
        teaLog.info(MESSAGE, THROWABLE);
        verify(mockLogger).info(MESSAGE, THROWABLE);
    }
    
    @Test
    public void warningErrorIsForwarded() {
        teaLog.warning(MESSAGE, THROWABLE);
        verify(mockLogger).warn(MESSAGE, THROWABLE);
    }
    
    @Test
    public void errorErrorIsForwarded() {
        teaLog.error(MESSAGE, THROWABLE);
        verify(mockLogger).error(MESSAGE, THROWABLE);
    }
    
}
