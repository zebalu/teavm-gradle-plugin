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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teavm.backend.wasm.render.WasmBinaryVersion;
import org.teavm.tooling.TeaVMTargetType;
import org.teavm.vm.TeaVMOptimizationLevel;

public class TeavmExtension {
	
	private static final Logger LOG = LoggerFactory.getLogger(TeavmExtension.class);
	
    private FileCollection classFiles = null;

    private List<String> compileScopes = null;
    
    private boolean minifying = true;

    private int maxTopLevelNames = 10000;

    private Properties properties = null;

    private boolean debugInformationGenerated = false;

    private boolean sourceMapsGenerated = false ;

    private boolean sourceFilesCopied = false;

    private boolean incremental = false;

    private String[] transformers = null;

    private File targetDirectory = null;

    private File sourceDirectory = null;

    private String targetFileName = "";

    private String mainClass;

    private String entryPointName;

    private String[] classesToPreserve;

    private boolean stopOnErrors = true;

    private TeaVMOptimizationLevel optimizationLevel = TeaVMOptimizationLevel.SIMPLE;

    private boolean fastGlobalAnalysis = false;

    private TeaVMTargetType targetType = TeaVMTargetType.JAVASCRIPT;

    private File cacheDirectory = null;

    private WasmBinaryVersion wasmVersion = WasmBinaryVersion.V_0x1;

    private int minHeapSize = 4;

    private int maxHeapSize= 128;

    private boolean outOfProcessv= false;

    private int processMemory = 512;

    private boolean longjmpSupported = true;

    private boolean heapDump = false;
    
    private List<String> includeJarsFrom = new ArrayList<>(Arrays.asList("runtimeClasspath"));
    
    private FileCollection extraLibs = null;
    
    private boolean skipJavaCompile = false;

	public FileCollection getClassFiles() {
		return classFiles;
	}

	public void setClassFiles(FileCollection classFiles) {
		this.classFiles = classFiles;
	}

	public List<String> getCompileScopes() {
		return compileScopes;
	}

	public void setCompileScopes(List<String> compileScopes) {
		this.compileScopes = compileScopes;
	}

	public boolean isMinifying() {
		return minifying;
	}

	public void setMinifying(boolean minifying) {
		this.minifying = minifying;
	}

	public int getMaxTopLevelNames() {
		return maxTopLevelNames;
	}

	public void setMaxTopLevelNames(int maxTopLevelNames) {
		this.maxTopLevelNames = maxTopLevelNames;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public boolean isDebugInformationGenerated() {
		return debugInformationGenerated;
	}

	public void setDebugInformationGenerated(boolean debugInformationGenerated) {
		this.debugInformationGenerated = debugInformationGenerated;
	}

	public boolean isSourceMapsGenerated() {
		return sourceMapsGenerated;
	}

	public void setSourceMapsGenerated(boolean sourceMapsGenerated) {
		this.sourceMapsGenerated = sourceMapsGenerated;
	}

	public boolean isSourceFilesCopied() {
		return sourceFilesCopied;
	}

	public void setSourceFilesCopied(boolean sourceFilesCopied) {
		this.sourceFilesCopied = sourceFilesCopied;
	}

	public boolean isIncremental() {
		return incremental;
	}

	public void setIncremental(boolean incremental) {
		this.incremental = incremental;
	}

	public String[] getTransformers() {
		return transformers;
	}

	public void setTransformers(String[] transformers) {
		this.transformers = transformers;
	}

	public File getTargetDirectory() {
		return targetDirectory;
	}

	public void setTargetDirectory(File targetDirectory) {
		this.targetDirectory = targetDirectory;
	}

	public File getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public String getTargetFileName() {
		return targetFileName;
	}

	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}

	public String getMainClass() {
		return mainClass;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	public String getEntryPointName() {
		return entryPointName;
	}

	public void setEntryPointName(String entryPointName) {
		this.entryPointName = entryPointName;
	}

	public String[] getClassesToPreserve() {
		return classesToPreserve;
	}

	public void setClassesToPreserve(String[] classesToPreserve) {
		this.classesToPreserve = classesToPreserve;
	}

	public boolean isStopOnErrors() {
		return stopOnErrors;
	}

	public void setStopOnErrors(boolean stopOnErrors) {
		this.stopOnErrors = stopOnErrors;
	}

	public TeaVMOptimizationLevel getOptimizationLevel() {
		return optimizationLevel;
	}

	public void setOptimizationLevel(TeaVMOptimizationLevel optimizationLevel) {
		this.optimizationLevel = optimizationLevel;
	}
	
	public void setOptimizationLevel(String optimizationLevel) {
		this.optimizationLevel = TeaVMOptimizationLevel.valueOf(optimizationLevel);
	}

	public boolean isFastGlobalAnalysis() {
		return fastGlobalAnalysis;
	}

	public void setFastGlobalAnalysis(boolean fastGlobalAnalysis) {
		this.fastGlobalAnalysis = fastGlobalAnalysis;
	}

	public TeaVMTargetType getTargetType() {
		return targetType;
	}

	public void setTargetType(TeaVMTargetType targetType) {
		this.targetType = targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = TeaVMTargetType.valueOf(targetType);
	}
	
	public File getCacheDirectory() {
		return cacheDirectory;
	}

	public void setCacheDirectory(File cacheDirectory) {
		this.cacheDirectory = cacheDirectory;
	}

	public WasmBinaryVersion getWasmVersion() {
		return wasmVersion;
	}

	public void setWasmVersion(WasmBinaryVersion wasmVersion) {
		this.wasmVersion = wasmVersion;
	}

	public void setWasmVersion(String wasmVersion) {
		this.wasmVersion = WasmBinaryVersion.valueOf(wasmVersion);
	}
	
	public int getMinHeapSize() {
		return minHeapSize;
	}

	public void setMinHeapSize(int minHeapSize) {
		this.minHeapSize = minHeapSize;
	}

	public int getMaxHeapSize() {
		return maxHeapSize;
	}

	public void setMaxHeapSize(int maxHeapSize) {
		this.maxHeapSize = maxHeapSize;
	}

	public boolean isOutOfProcessv() {
		return outOfProcessv;
	}

	public void setOutOfProcessv(boolean outOfProcessv) {
		this.outOfProcessv = outOfProcessv;
	}

	public int getProcessMemory() {
		return processMemory;
	}

	public void setProcessMemory(int processMemory) {
		this.processMemory = processMemory;
	}

	public boolean isLongjmpSupported() {
		return longjmpSupported;
	}

	public void setLongjmpSupported(boolean longjmpSupported) {
		this.longjmpSupported = longjmpSupported;
	}

	public boolean isHeapDump() {
		return heapDump;
	}

	public void setHeapDump(boolean heapDump) {
		this.heapDump = heapDump;
	}

	public List<String> getIncludeJarsFrom() {
		return includeJarsFrom;
	}

	public void setIncludeJarsFrom(List<String> includeJarsFrom) {
		this.includeJarsFrom = includeJarsFrom;
	}

	public FileCollection getExtraLibs() {
		return extraLibs;
	}

	public void setExtraLibs(FileCollection extraLibs) {
		this.extraLibs = extraLibs;
	}

	public boolean isSkipJavaCompile() {
		return skipJavaCompile;
	}

	public void setSkipJavaCompile(boolean skipJavaCompile) {
		this.skipJavaCompile = skipJavaCompile;
	}
	
	public void setMissingProjectDefaults(Project project) {
		if(targetDirectory == null) {
			targetDirectory = new File(project.getBuildDir(), "teavm");
			LOG.warn("teavm target directory is not set. Fall back to: {}", targetDirectory.getAbsolutePath());
		}
		if(sourceDirectory == null) {
			sourceDirectory = new File(project.getProjectDir(), "src/main/java");
			LOG.warn("teavm compile soruce directory is not set. Fall back to: {}", sourceDirectory.getAbsolutePath());
		}
		if(classFiles == null) {
			classFiles = project.files(new File(project.getBuildDir(), "classes/java/main"));
			LOG.warn("teavm classes directory is not set. Fall back to: {}", classFiles.getSingleFile().getAbsolutePath());
		}
	}
}
