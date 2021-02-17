import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FilesWalkFileTree {

    // C:\MyDocum\MyProjects\java_basics\09_FilesAndNetwork\DirectorySizeCounter\src\main\data
    // 09_FilesAndNetwork/DirectorySizeCounter/logs
    // C:\Users\kupag\Desktop

    public static void main(String[] args) {

        final Logger LOGGER = LogManager.getLogger(FilesWalkFileTree.class);
        final Marker INPUT_HISTORY_MARKER = MarkerManager.getMarker("INPUT_HISTORY");
        final Marker INVALID_INPUTS_MARKER = MarkerManager.getMarker("INVALID_INPUTS");
        final Marker EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
        final Marker DIRECTORY_SIZE_MARKER = MarkerManager.getMarker("DIRECTORY_SIZE");
        final Marker PRINT_MARKER = MarkerManager.getMarker("PRINT");

        for (; ; ) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите путь к папке :");
            String line = scanner.nextLine().trim();
            LOGGER.info(INPUT_HISTORY_MARKER, "Пользователь искал : " + line.trim());

            try {
                Path pathSource = Paths.get(line);
                long size = Files.walk(pathSource)
                        .map(Path::toFile)
                        .filter(File::isFile)
                        .mapToLong(File::length)
                        .sum();

                LOGGER.info(PRINT_MARKER, GetReadableSizeDirectory.getReadableFolderSize(size) + " - " + line.trim());
                LOGGER.info(DIRECTORY_SIZE_MARKER, GetReadableSizeDirectory.getReadableFolderSize(size) + " - " + line.trim());

            } catch (Exception ex) {
                ex.printStackTrace();

                LOGGER.error(EXCEPTION_MARKER, (Object) ex);
                LOGGER.warn(INVALID_INPUTS_MARKER, "Ошибочнычный поиск : " + line);
            }
        }
    }
}
