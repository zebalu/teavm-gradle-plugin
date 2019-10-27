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
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import org.gradle.api.DefaultTask;
import org.gradle.api.GradleException;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.InputDirectory;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teavm.diagnostics.Problem;
import org.teavm.tooling.builder.BuildException;
import org.teavm.tooling.builder.BuildResult;
import org.teavm.tooling.builder.BuildStrategy;
import org.teavm.tooling.builder.InProcessBuildStrategy;

public class TeavmCompileTask extends DefaultTask {

	private static final Logger LOG = LoggerFactory.getLogger(TeavmCompileTask.class);

	private BuildStrategy buildStrategy;

	private TeavmExtension settings = new TeavmExtension();

	@TaskAction
	public void compile() {
		setupBuild();
		executeBuild();
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

	private void setupBuild() {
		settings.setMissingProjectDefaults(getProject());
		buildStrategy = new InProcessBuildStrategy(URLClassLoader::newInstance);
		if (settings.getCacheDirectory() != null) {
			buildStrategy.setCacheDirectory(settings.getCacheDirectory().getAbsolutePath());
		}
		buildStrategy.addSourcesDirectory(settings.getSourceDirectory().getAbsolutePath());
		if (settings.getClassesToPreserve() != null) {
			buildStrategy.setClassesToPreserve(settings.getClassesToPreserve());
		}
		buildStrategy.setClassPathEntries(prepareClassPath());
		buildStrategy.setDebugInformationGenerated(settings.isDebugInformationGenerated());
		buildStrategy.setEntryPointName(settings.getEntryPointName());
		buildStrategy.setHeapSize(settings.getMaxHeapSize());
		buildStrategy.setIncremental(settings.isIncremental());
		buildStrategy.setLog(new Slf4JTeavmLog(TeavmCompileTask.class));
		buildStrategy.setLongjmpSupported(settings.isLongjmpSupported());
		buildStrategy.setMainClass(settings.getMainClass());
		buildStrategy.setMaxTopLevelNames(settings.getMaxTopLevelNames());
		buildStrategy.setMinifying(settings.isMinifying());
		buildStrategy.setOptimizationLevel(settings.getOptimizationLevel());
		buildStrategy.setProgressListener(new LoggingProgressListener(LOG));
		if (settings.getProperties() != null) {
			buildStrategy.setProperties(settings.getProperties());
		}
		buildStrategy.setSourceFilesCopied(settings.isSourceFilesCopied());
		buildStrategy.setSourceMapsFileGenerated(settings.isSourceMapsGenerated());
		buildStrategy.setTargetDirectory(settings.getTargetDirectory().getAbsolutePath());
		buildStrategy.setTargetFileName(settings.getTargetFileName());
		buildStrategy.setTargetType(settings.getTargetType());
	}

	private void executeBuild() {
		try {
			BuildResult result = buildStrategy.build();
			debugLogResult(result);
			handleProblemsInResult(result);
		} catch (BuildException e) {
			throw new GradleException("TeaVM compilation error", e);
		}

	}

	private void handleProblemsInResult(BuildResult result) {
		if (result.getProblems() != null) {
			handleSevereProblems(result);
			handleAllProblems(result);
		}
	}

	private void handleAllProblems(BuildResult result) {
		for (Problem p : result.getProblems().getProblems()) {
			LOG.info("problem: [{}] - {} : {} - {} params: {}", p.getSeverity(), p.getClass(), p.getLocation(),
					p.getText(), p.getParams());
		}
	}

	private void handleSevereProblems(BuildResult result) {
		StringJoiner errorMessages = new StringJoiner("\n");
		for (Problem sp : result.getProblems().getSevereProblems()) {
			LOG.error("severe problem: [{}] - {} : {} - {} params: {}", sp.getSeverity(), sp.getClass(),
					sp.getLocation(), sp.getText(), sp.getParams());
			errorMessages.add(sp.getText());
		}
		if (!result.getProblems().getSevereProblems().isEmpty() && settings.isStopOnErrors()) {
			throw new GradleException("TeaVM could not compile sources: " + errorMessages.toString());
		}
	}

	private void debugLogResult(BuildResult result) {
		LOG.debug("classes: {}", result.getClasses());
		LOG.debug("generated files: {}", result.getGeneratedFiles());
		LOG.debug("used resources: {}", result.getUsedResources());
	}

	public TeavmExtension getSettings() {
		return settings;
	}

	public void setSettings(TeavmExtension settings) {
		this.settings = settings;
	}

	private List<String> prepareClassPath() {
		LOG.info("Preparing classpath for TeaVM");
		List<String> paths = new ArrayList<>();
		addFileCollectionToPathes(paths, settings.getClassFiles());
		for (String configName : settings.getIncludeJarsFrom()) {
			addFileCollectionToPathes(paths, getProject().getConfigurations().getByName(configName));
		}
		addFileCollectionToPathes(paths, settings.getExtraLibs());
		logClasspath(paths);
		return paths;
	}

	private void addFileCollectionToPathes(List<String> pathes, FileCollection collection) {
		if (collection == null) {
			return;
		}
		for (File file : collection.getFiles()) {
			pathes.add(file.getAbsolutePath());
		}
	}

	private void logClasspath(List<String> paths) {
		if (!LOG.isInfoEnabled()) {
			return;
		}
		StringJoiner classPath = new StringJoiner(":");
		paths.forEach(classPath::add);
		LOG.info("Using the following classpath for TeaVM: {}", classPath.toString());
	}
}
