package io.github.zebalu.gradle.teavm;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.File;

import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;
import org.gradle.api.tasks.TaskContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.zebalu.gradle.teavm.TeavmGradlePlugin.TeavmCompileExecutorAction;

@ExtendWith(MockitoExtension.class)
public class TeavmGradlePluginTest {

    @Mock
    private Project project;
    @Mock
    private ExtensionContainer extensions;
    @Mock
    private TaskContainer taskContainer;
    private File buildDir = new File("build");
    private File projectDir = new File("");
    
    @Captor
    private ArgumentCaptor<TeavmExtension> extensionCaptor;
    
    private TeavmGradlePlugin plugin = new TeavmGradlePlugin();
    
    @BeforeEach
    public void initMock() {
        Mockito.when(project.getBuildDir()).thenReturn(buildDir);
        Mockito.when(project.getProjectDir()).thenReturn(projectDir);
        Mockito.when(project.getExtensions()).thenReturn(extensions);
        Mockito.when(project.getTasks()).thenReturn(taskContainer);
    }
    
    @Test
    public void pluginCanBeApplied() {
        assertDoesNotThrow(()->plugin.apply(project));
    }
    
    @Test
    public void applyMakesExtension() {
        plugin.apply(project);
        Mockito.verify(extensions).add(Mockito.eq("teavm"), Mockito.any(TeavmExtension.class));
    }
    
    @Test
    public void extensionSetUpCorrectly() {
        plugin.apply(project);
        Mockito.verify(extensions).add(Mockito.anyString(), extensionCaptor.capture());
        TeavmExtension extension = extensionCaptor.getValue();
        assertAll(()->{
            assertEquals(extension.getCacheDirectory(), new File(buildDir, "teavm-cache"));
        });
    }
    
    @Test
    public void configSetsExtension() {
        plugin.apply(project);
        Mockito.verify(extensions).add(Mockito.anyString(), extensionCaptor.capture());
        TeavmExtension extension = extensionCaptor.getValue();
        TeavmCompileExecutorAction executor = plugin.new TeavmCompileExecutorAction();
        TeavmCompileTask tct = Mockito.mock(TeavmCompileTask.class);
        ArgumentCaptor<TeavmExtension> secondCaptor = ArgumentCaptor.forClass(TeavmExtension.class);
        executor.execute(tct);
        Mockito.verify(tct).setSettings(secondCaptor.capture());
        assertSame(extension, secondCaptor.getValue());
    }
    
    @Test
    public void teavmcTaskISRegistered() {
        plugin.apply(project);
        Mockito.verify(taskContainer).register(Mockito.eq("teavmc"), Mockito.eq(TeavmCompileTask.class), Mockito.any(TeavmGradlePlugin.TeavmCompileExecutorAction.class));
    }
}
