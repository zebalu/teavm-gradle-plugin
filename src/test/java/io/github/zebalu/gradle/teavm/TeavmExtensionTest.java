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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
        assertDoesNotThrow(() -> teavmExtension.setOptimizationLevel("ADVANCED"));
    }

    @Test
    public void targetTypeCanBeSetByString() {
        assertDoesNotThrow(() -> teavmExtension.setTargetType("JAVASCRIPT"));
    }

    @Test
    public void skipJavaCompileIsFalseByDefault() {
        assertFalse(teavmExtension.isSkipJavaCompile());
    }

    @Test
    public void externalizationTest() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(new TeavmExtension());
        oos.close();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
        TeavmExtension teaExtension = (TeavmExtension) ois.readObject();
        assertNotNull(teaExtension);
    }
}
