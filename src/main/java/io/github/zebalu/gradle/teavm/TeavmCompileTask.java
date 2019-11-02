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

import java.io.File;
import java.net.URLClassLoader;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.teavm.tooling.builder.BuildStrategy;
import org.teavm.tooling.builder.InProcessBuildStrategy;

/**
 * An adapter class to have a gradle task to execute {@link BuildStrategy} of
 * TeaVM tool.
 * 
 * @author zebalu
 *
 */
public class TeavmCompileTask extends DefaultTask {

    private TeavmExtension settings = new TeavmExtension();

    /**
     * This variable holds every setup you can do with a TeavmCompileTask.
     * 
     * @return
     */
    public TeavmExtension getSettings() {
        return settings;
    }

    public void setSettings(TeavmExtension settings) {
        this.settings = settings;
    }

    @TaskAction
    public void compile() {
        settings.setMissingProjectDefaults(getProject());
        TeavmCompileExecutor executor = new TeavmCompileExecutor(
                settings,
                new InProcessBuildStrategy(URLClassLoader::new),
                name -> getProject().getConfigurations().getByName(name)
            );
        executor.executeCompile();
    }

    @OutputDirectory
    public File getTargetDirectory() {
        settings.setMissingProjectDefaults(getProject());
        return settings.getTargetDirectory();
    }

    @InputDirectory
    public File getSourceDirectory() {
        settings.setMissingProjectDefaults(getProject());
        return settings.getSourceDirectory();
    }
}
