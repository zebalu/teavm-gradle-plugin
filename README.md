# teavm-gradle-plugin
Gradle plugin to compile jvm languages (bytecode) to Javascript (/Web assembly /C)

[![Build Status](https://travis-ci.com/zebalu/teavm-gradle-plugin.svg?branch=master)](https://travis-ci.com/zebalu/teavm-gradle-plugin)

# What is TeaVM?

TeaVM is a nice tool to compile (transpile? decompile? detrasnpile?) bytecode into JavaScript.
You can find more details on its website: http://teavm.org/ Find examples in its git repo: 
https://github.com/konsoletyper/teavm or find this nice description in Java Magagzine: 
https://blogs.oracle.com/javamagazine/java-in-the-browser-with-teavm 

# How to use

    apply plugin: 'java'
    apply plugin: 'io.github.zebalu.teavm-gradle-plugin'
    
    buildscript {
        repositories {
            jcenter()
        }
    
        dependencies {
            classpath 'io.github.zebalu:teavm-gradle-plugin:+'
        }
    }
    
this is the minimal setup to use the plugin. (Compile task is `teavmc`.)

# How to set up

By default the plugin adds `teavmc` task to your project. You can set it up
by `teavm` extension object. [source](https://github.com/zebalu/teavm-gradle-plugin/blob/master/src/main/java/io/github/zebalu/gradle/teavm/TeavmExtension.java)

It goes like this (every value is the default):

    teavm {
       /** It a FileCollection object with all the calssfiles to use in compilation */
       classFiles = null;
       
       compileScopes = null;
       minifying = true;
       maxTopLevelNames = 10000;
       properties = null;
       debugInformationGenerated = false;
       sourceMapsGenerated = false;
       sourceFilesCopied = false;
       incremental = false;
       transformers = null;
       
       /** Where to save the result */
       targetDirectory = null;
       
       /** The directory to monitor to decide if compile is up-to-date or not
       sourceDirectory = null;
       
       /** How to name the result file. */
       targetFileName = "classes.js";
       
       /** Which class holds your public static void main(Strin[] args) method */
       mainClass = null;
       
       /** This will be the name of your main method after compilation. */
       entryPointName = 'main';
       
       classesToPreserve = null;
       stopOnErrors = true;
       optimizationLevel = TeaVMOptimizationLevel.SIMPLE;
       fastGlobalAnalysis = false;
       targetType = TeaVMTargetType.JAVASCRIPT;
       cacheDirectory = null;
       wasmVersion = WasmBinaryVersion.V_0x1;
       minHeapSize = 4;
       maxHeapSize = 128;
       outOfProcess = false;
       processMemory = 512;
       longjmpSupported = true;
       heapDump = false;
       
       /** Add name of configurations here where to look for jarfiles. */
       includeJarsFrom = ['runtimeClasspath'];
       
       /** FileCollection object which adds extra jars to compilation. */
       extraLibs = null;
       
       /** By default teavmc taskd epends on javaCompile task, unless this varaibale is true. /
       skipJavaCompile = false;       
    }
    
All the undocumented stuff is unclear for me. I just use the original author's settings. (Sorry.)

# How to create a custom task?

You should crate a TeavmCompileTask and every setting can be passed to the `settings` object:

    task myCupOfTea(type: io.github.zebalue.gradle.teavm.TeavmCompileTask) {
        settings.skipJavaCompile=true
        settings.entryPointName='leEntry'
        /** etc. */
    }
    
