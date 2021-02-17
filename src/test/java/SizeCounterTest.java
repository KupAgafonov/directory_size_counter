import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.StreamSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SizeCounterTest {

    private static long getFolderSize(File folder) {
        long length = 0;
        File[] files = folder.listFiles();

        int count = files.length;

        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                length += files[i].length();
            } else {
                length += getFolderSize(files[i]);
            }
        }
        return length;
    }

    @Test // getFolderSize()
    public void whenGetFolderSizeRecursive() {

        long expectedSize = 30108;

        File folder = new File("C:\\MyDocum\\MyProjects\\java_basics\\09_FilesAndNetwork\\DirectorySizeCounter\\src\\main\\data");
        long size = getFolderSize(folder);

        assertEquals(expectedSize, size);
    }

    @Test
    public void whenGetFolderSizeUsingJava7() throws IOException {
        long expectedSize = 30108;

        AtomicLong size = new AtomicLong(0);
        Path folder = Paths.get("C:\\MyDocum\\MyProjects\\java_basics\\09_FilesAndNetwork\\DirectorySizeCounter\\src\\main\\data");

        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                size.addAndGet(attrs.size());
                return FileVisitResult.CONTINUE;
            }
        });

        assertEquals(expectedSize, size.longValue());
    }

    @Test
    public void whenGetFolderSizeUsingJava8() throws IOException {
        long expectedSize = 30108;

        Path folder = Paths.get("C:\\MyDocum\\MyProjects\\java_basics\\09_FilesAndNetwork\\DirectorySizeCounter\\src\\main\\data");
        long size = Files.walk(folder)
                .filter(p -> p.toFile().isFile())
                .mapToLong(p -> p.toFile().length())
                .sum();

        assertEquals(expectedSize, size);
    }

    @Test
    public void whenGetFolderSizeUsingApacheCommonsIO() {
        long expectedSize = 30108;

        File folder = new File("C:\\MyDocum\\MyProjects\\java_basics\\09_FilesAndNetwork\\DirectorySizeCounter\\src\\main\\data");
        long size = FileUtils.sizeOfDirectory(folder);

        assertEquals(expectedSize, size);
    }

    @Test
    public void whenGetFolderSizeUsingGuava() {
        long expectedSize = 30108;

        File folder = new File("C:\\MyDocum\\MyProjects\\java_basics\\09_FilesAndNetwork\\DirectorySizeCounter\\src\\main\\data");

        Iterable<File> files = com.google.common.io.Files.fileTreeTraverser()
                .breadthFirstTraversal(folder);
        long size = StreamSupport.stream(files.spliterator(), false)
                .filter(f -> f.isFile())
                .mapToLong(File::length).sum();

        assertEquals(expectedSize, size);
    }

    @Test
    public void whenGetReadableSize() {

        File folder = new File("C:\\MyDocum\\MyProjects\\java_basics\\09_FilesAndNetwork\\DirectorySizeCounter\\src\\main\\data");
        long size = getFolderSize(folder);

        String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int unitIndex = (int) (Math.log10(size) / 3);
        double unitValue = 1 << (unitIndex * 10);

        String readableSize = new DecimalFormat("#,##0.#")
                .format(size / unitValue) + " "
                + units[unitIndex];
        assertEquals("29,4 KB", readableSize);
    }


}
