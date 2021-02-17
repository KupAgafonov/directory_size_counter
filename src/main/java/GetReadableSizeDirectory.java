import java.text.DecimalFormat;

public class GetReadableSizeDirectory {

    protected static String getReadableFolderSize(long size) {

            String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
            int unitIndex = (int) (Math.log10(size) / 3);
            double unitValue = 1 << (unitIndex * 10);

            return new DecimalFormat("#,##0.#")
                    .format(size / unitValue) + " "
                    + units[unitIndex];
    }
}

