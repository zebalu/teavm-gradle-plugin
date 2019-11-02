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

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * This plugin adds teavmc task to project and teavm extension object.
 * 
 * @author zebalu
 *
 */
public class TeavmGradlePlugin implements Plugin<Project> {

    private Project project;
    private TeavmExtension settings;

    @Override
    public void apply(Project project) {
        this.project = project;
        registerExtension();
        registerTask();
    }

    private void registerTask() {
        project.getTasks().register("teavmc", TeavmCompileTask.class, new TeavmCompileExecutorAction());
    }

    private void registerExtension() {
        settings = new TeavmExtension();
        settings.setClassFiles(project.files(new File(project.getBuildDir(), "classes/java/main")));
        settings.setTargetDirectory(new File(project.getBuildDir(), "teavm"));
        settings.setCacheDirectory(new File(project.getBuildDir(), "teavm-cache"));
        settings.setSourceDirectory(new File(project.getProjectDir(), "src/main/java"));
        project.getExtensions().add("teavm", settings);
    }
    
    public class TeavmCompileExecutorAction implements Action<TeavmCompileTask> {

        @Override
        public void execute(TeavmCompileTask teavmCompileTask) {
            teavmCompileTask.setSettings(settings);
            if (!settings.isSkipJavaCompile()) {
                teavmCompileTask.dependsOn(project.getTasks().getByName("compileJava"));
                teavmCompileTask.setGroup("TeaVM");
            }
        }
        
    }
}