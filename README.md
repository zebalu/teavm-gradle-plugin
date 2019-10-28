# teavm-gradle-plugin
Gradle plugin to compile jvm languages (bytecode) to Javascript (/Web assembly /C)

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