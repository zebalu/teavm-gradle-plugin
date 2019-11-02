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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

public class CopyUtil {

    private final static int _16MB = 16 * 1024 * 1024;

    public static void copy(File from, File to) throws FileNotFoundException, IOException {
        copy(from, to, _16MB);
    }

    public static void copy(File from, File to, int bufferSize) throws FileNotFoundException, IOException {
        Copy copy = new Copy(from, to, bufferSize);
        copy.execute();
    }

    private static class Copy {
        private final byte[] buffer;

        private final File sourceBase;
        private final File targetBase;

        private final Queue<File> copyQueue;

        private Copy(File source, File target, int bufferSize) {
            sourceBase = source.getParentFile();
            targetBase = target;
            buffer = new byte[bufferSize];
            copyQueue = new ArrayDeque<>();
            copyQueue.add(source);
        }

        void execute() throws FileNotFoundException, IOException {
            while (!copyQueue.isEmpty()) {
                copyFile(copyQueue.peek(), toTargetFile(copyQueue.poll()));
            }
        }
        
        private void copyFile(File toCopy, File target) throws FileNotFoundException, IOException {
            if (toCopy.isDirectory()) {
                directoryCopy(toCopy, target);
            } else {
                binaryFileCopy(toCopy, target);
            }
        }

        private void directoryCopy(File toCopy, File target) {
            target.mkdirs();
            for (File file : toCopy.listFiles()) {
                copyQueue.add(file);
            }
        }

        private File toTargetFile(File baseFile) {
            String relativePath = baseFile.getAbsolutePath().substring(sourceBase.getAbsolutePath().length() + 1);
            return new File(targetBase, relativePath);
        }

        private void binaryFileCopy(File source, File target) throws FileNotFoundException, IOException {
            try (FileInputStream fis = new FileInputStream(source);
                    FileOutputStream fos = new FileOutputStream(target)) {
                copyStreams(fis, fos);
            }
        }

        private void copyStreams(FileInputStream inputStream, FileOutputStream outputStream) throws IOException {
            int readBytesCount = -1;
            while ((readBytesCount = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readBytesCount);
            }
        }
    }
}
