package data;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class IOTest {

    public boolean compareImagesBytes(InputStream first, InputStream second, Shape shape) throws IOException {
        BMP.readHeader(first);
        BMP.readHeader(second);
        for (int i = 0; i < shape.height; i++) {
            for (int j = 0; j < shape.width * 3; j++) {
                byte a = (byte) first.read();
                byte b = (byte) second.read();
                if (a != b) {
                    return false;
                }
            }
            for (int j = 0; (shape.width * 3 + j) % 4 != 0; j++) {
                first.read();
                second.read();
            }
        }
        return true;
    }

    public boolean compareImagesArray(InputStream first, InputStream second, Shape shape) throws IOException {
        BMP.readHeader(first);
        BMP.readHeader(second);
        byte[] row1 = new byte[3 * shape.width];
        byte[] row2 = new byte[3 * shape.width];
        for (int i = 0; i < shape.height; i++) {
            int last = 0;
            while (last != 3 * shape.width) {
                last += first.read(row1, last, 3 * shape.width - last);
            }
            last = 0;
            while (last != 3 * shape.width) {
                last += second.read(row2, last, 3 * shape.width - last);
            }
            for (int j = 0; (shape.width * 3 + j) % 4 != 0; j++) {
                first.read();
                second.read();
            }
            if (Arrays.compare(row1, row2) != 0) {
                return false;
            }
        }
        return true;
    }
}
