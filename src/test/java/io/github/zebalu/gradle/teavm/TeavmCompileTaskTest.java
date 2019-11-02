package io.github.zebalu.gradle.teavm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

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
        assumeFalse(new File("").getAbsolutePath().contains("travis"), "This test can not run on travis ci, skipping");
        CopyUtil.copy(new File(inputDir, projectName), builDir);
    }

    @Test
    public void teavmcCanBeRun() throws Exception {
        BuildResult result = GradleRunner.create().withProjectDir(new File(builDir, projectName)).withPluginClasspath()
                .withArguments("teavmc").withDebug(true).build();
        assertEquals(TaskOutcome.SUCCESS, result.task(":teavmc").getOutcome());
    }
}
