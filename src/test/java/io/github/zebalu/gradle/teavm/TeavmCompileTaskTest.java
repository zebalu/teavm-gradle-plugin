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

import java.io.File;

import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TeavmCompileTaskTest {

    @TempDir
    public File builDir;

    private File inputDir = new File("src/test/projects/");
    private String projectName = "compileCanBeRun";

    @BeforeEach
    public void copyTemplateProject() throws Exception {
        CopyUtil.copy(new File(inputDir, projectName), builDir);
    }

    @Test
    public void teavmcCanBeRun() throws Exception {
        BuildResult result = GradleRunner.create().withProjectDir(new File(builDir, projectName)).withPluginClasspath()
                .withArguments("teavmc").withDebug(true).build();
        assertEquals(TaskOutcome.SUCCESS, result.task(":teavmc").getOutcome());
    }
}
