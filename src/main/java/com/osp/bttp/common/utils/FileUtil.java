package com.osp.bttp.common.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

    /**
     * Save files in the upload directory.
     * Ex: uploadDir = "/home/dir", 1234_file1.png, multipartFile -> `1234_file1.png` saved into "/home/dir"
     */
    public static String uploadFile(String containerName, String dir,
                                    String fileName, InputStream inputStream) throws IOException {
        Path uploadPath = Paths.get(containerName + "/" + dir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        return filePath.toString();
    }

    /**
     * Only delete files in the upload directory, not delete folders.
     * Ex: file1, folder2(file1, file2) -> delete file 1, file 1, 2 in folder2 remaining
     */
    public static void cleanDir(String dir) {
        Path dirPath = Paths.get(dir);

        try (Stream<Path> files = Files.list(dirPath)) {
            files.forEach(file -> {
                if (!Files.isDirectory(file)) {
                    try {
                        Files.delete(file);
                    } catch (IOException ex) {
                        log.error("Could not delete file: {} due to {}", file, ex.getMessage());
                    }
                }
            });
        } catch (IOException ex) {
            log.error("Could not list directory: {} due to {}", dirPath, ex.getMessage());
        }
    }

    public static void removeDir(String dir) {
        cleanDir(dir);
        try {
            Files.delete(Paths.get(dir));
        } catch (IOException e) {
            log.error("Could not remove directory: {}", dir);
        }

    }

    public static byte[] readFile(String pathFile) throws IOException {
        Path path = Paths.get(pathFile);
        return Files.readAllBytes(path);
    }
}
