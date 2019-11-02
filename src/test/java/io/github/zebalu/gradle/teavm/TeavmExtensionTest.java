package io.github.zebalu.gradle.teavm;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.BDDMockito.*;

import java.io.File;

import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TeavmExtensionTest {
    @Mock
    private Project project;
    
    private ConfigurableFileCollection fileCollection;
    
    @InjectMocks
    private TeavmExtension teavmExtension;

    @BeforeEach
    public void setupProject() {
        fileCollection = mock(ConfigurableFileCollection.class);
    }
    
    @Test
    public void extensionSetsUpMissingPartsFromProject() {
        given(project.getBuildDir()).willReturn(new File(""));
        given(project.getProjectDir()).willReturn(new File(""));
        given(project.files(any())).willReturn(fileCollection);
        given(fileCollection.getSingleFile()).willReturn(new File(""));
        
        teavmExtension.setMissingProjectDefaults(project);
        
        then(project).should(times(2)).getBuildDir();
        then(project).should(times(1)).getProjectDir();
    }
    
    @Test
    public void correctlySetUpExtensionIsNotChanged() {
        teavmExtension.setSourceDirectory(new File(""));
        teavmExtension.setTargetDirectory(new File(""));
        teavmExtension.setClassFiles(fileCollection);
        teavmExtension.setMissingProjectDefaults(project);
        then(project).shouldHaveNoInteractions();
    }
    
    @Test
    public void optimizationLevelCanBeSetByString() {
        assertDoesNotThrow(()->teavmExtension.setOptimizationLevel("ADVANCED"));
    }
    
    @Test
    public void targetTypeCanBeSetByString() {
        assertDoesNotThrow(()->teavmExtension.setTargetType("JAVASCRIPT"));
    }
    
    @Test
    public void skipJavaCompileIsFalseByDefault() {
        assertFalse(teavmExtension.isSkipJavaCompile());
    }
}
