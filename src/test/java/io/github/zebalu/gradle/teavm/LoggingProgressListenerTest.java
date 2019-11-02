/*  Copyright 2019 Balázs Zaicsek
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package io.github.zebalu.gradle.teavm;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.teavm.vm.TeaVMPhase;
import org.teavm.vm.TeaVMProgressFeedback;

@ExtendWith(MockitoExtension.class)
public class LoggingProgressListenerTest {

    @Mock
    private Logger mockLogger;
    
    @InjectMocks
    private LoggingProgressListener progressListener;
    
    @Test
    public void phaseStartIsForwardedToLogger() throws Exception {
        progressListener.phaseStarted(TeaVMPhase.DEPENDENCY_ANALYSIS, 2);
        verify(mockLogger).info(anyString(), eq(TeaVMPhase.DEPENDENCY_ANALYSIS), eq(2));
    }
    
    @Test
    public void progressIsForwardedToLogger() throws Exception {
        progressListener.phaseStarted(TeaVMPhase.COMPILING, 10);
        progressListener.progressReached(5);
        verify(mockLogger).info(anyString(), eq(TeaVMPhase.COMPILING), eq(10));
        verify(mockLogger).info(anyString(), eq(TeaVMPhase.COMPILING), eq(5), eq(10), eq(50));
    }
    
    @Test
    public void phaseStartReturnsContinue() {
        TeaVMProgressFeedback result = progressListener.phaseStarted(TeaVMPhase.DEPENDENCY_ANALYSIS, 2);
        assertEquals(TeaVMProgressFeedback.CONTINUE, result);
    }
    
    @Test
    public void phaseReachedReturnsContinue() {
        TeaVMProgressFeedback result = progressListener.progressReached(8);
        assertEquals(TeaVMProgressFeedback.CONTINUE, result);
    }
    
}
