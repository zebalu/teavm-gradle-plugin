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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.gradle.api.Project;
import org.gradle.api.file.FileCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.teavm.backend.wasm.render.WasmBinaryVersion;
import org.teavm.tooling.TeaVMTargetType;
import org.teavm.vm.TeaVMOptimizationLevel;

/**
 * Extension calss to set up TeaVM compiler.
 * 
 * @author zebalu
 */
public class TeavmExtension implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Logger LOG = LoggerFactory.getLogger(TeavmExtension.class);

    private Set<File> classFiles = null;

    private List<String> compileScopes = null;

    private boolean minifying = true;

    private int maxTopLevelNames = 10000;

    private Properties properties = null;

    private boolean debugInformationGenerated = false;

    private boolean sourceMapsGenerated = false;

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

    private int maxHeapSize = 128;

    private boolean outOfProcess = false;

    private int processMemory = 512;

    private boolean longjmpSupported = true;

    private boolean heapDump = false;

    private List<String> includeJarsFrom = new ArrayList<>(Arrays.asList("runtimeClasspath"));

    private Set<File> extraLibs = null;

    private boolean skipJavaCompile = false;

    /**
     * List of (jar)files/folders where input .class files can be found.
     * 
     * @return
     */
    public Set<File> getClassFiles() {
        return classFiles;
    }

    /**
     * Set list of class files to compile with TeaVM.
     * 
     * @param classFiles
     */
    public void setClassFiles(FileCollection classFiles) {
        if(classFiles == null) {
            this.classFiles=null;
        } else {
            setClassFiles(classFiles.getFiles());
        }
    }

    /**
     * Set list of class files to compile with TeaVM.
     * 
     * @param classFiles
     */
    public void setClassFiles(Set<File> classFiles) {
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

    /**
     * Number of max kept name. (Default is 10 000)
     * 
     * @return
     */
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

    /**
     * Where to save the result file.
     * 
     * @return
     */
    public File getTargetDirectory() {
        return targetDirectory;
    }

    public void setTargetDirectory(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }

    /**
     * This folder hold the original java files. This is only for the plugin to
     * monitor whether recompilation is needed or not.
     * 
     * @return
     */
    public File getSourceDirectory() {
        return sourceDirectory;
    }

    public void setSourceDirectory(File sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    /**
     * How to name the resulting file. Default is classes.js
     * 
     * @return
     */
    public String getTargetFileName() {
        return targetFileName;
    }

    public void setTargetFileName(String targetFileName) {
        this.targetFileName = targetFileName;
    }

    /**
     * The name of the main class (this will be the entrypoint of your JavaScript.)
     * 
     * @return
     */
    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    /**
     * The name of the javascript method to start your app. (Default is main()).
     * Always use together with mainClass
     * 
     * @return
     */
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

    /**
     * If false we don't throw gradle exception on teavm compile errors.
     */
    public boolean isStopOnErrors() {
        return stopOnErrors;
    }

    public void setStopOnErrors(boolean stopOnErrors) {
        this.stopOnErrors = stopOnErrors;
    }

    /**
     * The current optimization level. Options are:
     * {@link TeaVMOptimizationLevel#ADVANCED}, {@link TeaVMOptimizationLevel#FULL},
     * {@link TeaVMOptimizationLevel#SIMPLE}. Default is SIMPLE
     * 
     * @return
     */
    public TeaVMOptimizationLevel getOptimizationLevel() {
        return optimizationLevel;
    }

    public void setOptimizationLevel(TeaVMOptimizationLevel optimizationLevel) {
        this.optimizationLevel = optimizationLevel;
    }

    public void setOptimizationLevel(String optimizationLevel) {
        setOptimizationLevel(TeaVMOptimizationLevel.valueOf(optimizationLevel));
    }

    public boolean isFastGlobalAnalysis() {
        return fastGlobalAnalysis;
    }

    public void setFastGlobalAnalysis(boolean fastGlobalAnalysis) {
        this.fastGlobalAnalysis = fastGlobalAnalysis;
    }

    /**
     * Returns the {@link TeaVMTargetType} Options are:
     * {@link TeaVMTargetType#JAVASCRIPT} (default),
     * {@link TeaVMTargetType#WEBASSEMBLY}, {@link TeaVMTargetType#C} (highly
     * experimental, as I know)
     * 
     * @return
     */
    public TeaVMTargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TeaVMTargetType targetType) {
        this.targetType = targetType;
    }

    public void setTargetType(String targetType) {
        setTargetType(TeaVMTargetType.valueOf(targetType));
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

    /**
     * Not used currently.
     * 
     * @return
     */
    public boolean isOutOfProcess() {
        return outOfProcess;
    }

    public void setOutOfProcess(boolean outOfProcess) {
        this.outOfProcess = outOfProcess;
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

    /**
     * List the gradle configurations what we use to get jar files for TeaVM
     * compilations. Default is: ['runtimeClasspath']
     * 
     * @return
     */
    public List<String> getIncludeJarsFrom() {
        return includeJarsFrom;
    }

    public void setIncludeJarsFrom(List<String> includeJarsFrom) {
        this.includeJarsFrom = includeJarsFrom;
    }

    /**
     * List of additional jar files.
     * 
     * @return
     */
    public Set<File> getExtraLibs() {
        return extraLibs;
    }

    public void setExtraLibs(FileCollection extraLibs) {
        if(extraLibs == null) {
            this.extraLibs=null;
        } else {
            setExtraLibs(extraLibs.getFiles());
        }
    }
    
    public void setExtraLibs(Set<File> extraLibs) {
        this.extraLibs = extraLibs;
    }

    /**
     * If set to true than the default task does not depend on Java compile.
     * 
     * @return
     */
    public boolean isSkipJavaCompile() {
        return skipJavaCompile;
    }

    public void setSkipJavaCompile(boolean skipJavaCompile) {
        this.skipJavaCompile = skipJavaCompile;
    }

    /**
     * This method is called in various places where a {@link Project} object is
     * already available to set up defaults that might be missing. Non null values
     * are not touched. (This method is idempotent, you can call as many times as
     * you want, the result will be the same.
     * 
     * @param project
     */
    public void setMissingProjectDefaults(Project project) {
        if (targetDirectory == null) {
            targetDirectory = new File(project.getBuildDir(), "teavm");
            LOG.warn("teavm target directory is not set. Fall back to: {}", targetDirectory.getAbsolutePath());
        }
        if (sourceDirectory == null) {
            sourceDirectory = new File(project.getProjectDir(), "src/main/java");
            LOG.warn("teavm compile soruce directory is not set. Fall back to: {}", sourceDirectory.getAbsolutePath());
        }
        if (classFiles == null) {
            File fallbackFile = new File(project.getBuildDir(), "classes/java/main");
            classFiles = Collections.singleton(fallbackFile);
            LOG.warn("teavm classes directory is not set. Fall back to: {}",
                    fallbackFile.getAbsolutePath());
        }
    }
}
