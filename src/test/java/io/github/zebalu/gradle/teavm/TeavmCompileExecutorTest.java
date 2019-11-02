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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.teavm.diagnostics.Problem;
import org.teavm.diagnostics.ProblemProvider;
import org.teavm.tooling.TeaVMTargetType;
import org.teavm.tooling.builder.BuildResult;
import org.teavm.tooling.builder.BuildStrategy;

import io.github.zebalu.gradle.teavm.TeavmCompileExecutor.FileCollectionResolver;

@ExtendWith(MockitoExtension.class)
public class TeavmCompileExecutorTest {
    @Mock
    private FileCollectionResolver resolver;

    @Mock
    private FileCollection collection;

    @Mock
    private BuildResult result;

    @Mock
    private ProblemProvider problems;

    private TeavmExtension settings = new TeavmExtension();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private BuildStrategy strategy;
    private TeavmCompileExecutor teavmCompileExecutor;

    @BeforeEach
    public void setupExtension() {
        File dir = new File("");
        settings.setSourceDirectory(dir);
        settings.setTargetDirectory(dir);
        settings.setClassFiles(collection);
        teavmCompileExecutor = new TeavmCompileExecutor(settings, strategy, resolver);
    }

    @Test
    public void testExecuteCompile() throws Exception {
        given(strategy.build()).willReturn(result);
        given(result.getProblems()).willReturn(problems);
        given(problems.getSevereProblems()).willReturn(new ArrayList<>());
        given(problems.getProblems()).willReturn(new ArrayList<>());
        teavmCompileExecutor.executeCompile();
        then(strategy).should().setTargetType(any(TeaVMTargetType.class));
        then(strategy).should().build();
    }

    @Test
    public void severeProblemsBreaksBuild() throws Exception {
        given(strategy.build()).willReturn(result);
        given(result.getProblems()).willReturn(problems);
        given(problems.getSevereProblems()).willReturn(Arrays.asList(mock(Problem.class)));
        assertThrows(GradleException.class, () -> teavmCompileExecutor.executeCompile());
    }

    @Test
    public void extraLibsAreAddedToPathes() throws Exception {
        FileCollection extraLibs = mock(FileCollection.class);
        File extraLib = new File("/file/path/to/extra/jar");
        given(extraLibs.getFiles()).willReturn(Collections.singleton(extraLib));
        settings.setExtraLibs(extraLibs);
        given(strategy.build()).willReturn(result);
        given(result.getProblems()).willReturn(problems);
        given(problems.getSevereProblems()).willReturn(new ArrayList<>());
        given(problems.getProblems()).willReturn(new ArrayList<>());
        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<String>> stringListCaptor = ArgumentCaptor.forClass(List.class);

        teavmCompileExecutor.executeCompile();
        then(strategy).should().setClassPathEntries(stringListCaptor.capture());
        assertEquals(extraLib.getAbsolutePath(), stringListCaptor.getValue().get(0));
    }

}
