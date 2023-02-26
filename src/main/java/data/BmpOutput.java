package data;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class BmpOutput extends BmpIO {
    private final RandomAccessFile image;

    protected BmpOutput(RandomAccessFile image, Shape imageShape, Rectangle fragment) throws IOException {
        super(image, imageShape, fragment);
        this.image = image;
    }

    public void write(InputStream is) throws IOException {
        BMP.readHeader(is);
        for (int i = 0; i < fragment.height; i++) {
            if (!checkLine()) {
                return;
            }
            readRow(is);
            if (curY < 0 || curY >= height) {
                curX += 3 * fragment.width;
                continue;
            }
            image.write(buffer, Math.max(-curX, 0), Math.min(3 * fragment.width + Math.min(curX, 0), 3 * width - curX));
            curX += 3 * fragment.width;
        }
        close();
    }

    private void readRow(InputStream is) throws IOException {
        int last = 0;
        while (last != buffer.length) {
            last += is.read(buffer, last, buffer.length - last);
        }
    }
}
