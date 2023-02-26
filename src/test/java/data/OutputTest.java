package data;

import exceptions.ShapeSizeException;
import org.junit.jupiter.api.Test;

import java.io.*;

public class OutputTest extends IOTest {
    @Test
    void testImage() throws ShapeSizeException, IOException {

        File temp = new File("target/generated-test-sources");
        temp.mkdirs();

        // (Image stored bottom-up)
        // y' = imageHeight - y - fragmentHeight

        set("src/test/assets/350x234/origin.bmp",
                "target/generated-test-sources/test",
                new Shape(350, 234),
                new Rectangle(0, 234 - 0 - 234, 350, 234));
        assert (compareImagesArray(new FileInputStream("src/test/assets/350x234/origin.bmp"),
                new FileInputStream("target/generated-test-sources/test"), new Shape(350, 234)));
        File file = new File("target/generated-test-sources/test");
        file.delete();

        set("src/test/assets/350x234/origin.bmp",
                "target/generated-test-sources/test",
                new Shape(350, 234),
                new Rectangle(50, 234 - 100 - 234, 350, 234));
        assert (compareImagesArray(new FileInputStream("src/test/assets/350x234/set/50x100.bmp"),
                new FileInputStream("target/generated-test-sources/test"), new Shape(350, 234)));

        set("src/test/assets/350x234/origin.bmp",
                "target/generated-test-sources/test",
                new Shape(350, 234),
                new Rectangle(100, 234 - 50 - 234, 350, 234));
        assert (compareImagesArray(new FileInputStream("src/test/assets/350x234/set/50x100,100x50.bmp"),
                new FileInputStream("target/generated-test-sources/test"), new Shape(350, 234)));

        set("src/test/assets/350x234/origin.bmp",
                "target/generated-test-sources/test",
                new Shape(350, 234),
                new Rectangle(-200, 234 - (-50) - 234, 350, 234));
        assert (compareImagesArray(new FileInputStream("src/test/assets/350x234/set/50x100,100x50,-200x-50.bmp"),
                new FileInputStream("target/generated-test-sources/test"), new Shape(350, 234)));
        file = new File("target/generated-test-sources/test");
        file.delete();

        set("src/test/assets/2730x4096/origin.bmp",
                "target/generated-test-sources/test",
                new Shape(2730, 4096),
                new Rectangle(1234, -3456, 2730, 4096));
        assert (compareImagesArray(new FileInputStream("src/test/assets/2730x4096/set/1234x3456.bmp"),
                new FileInputStream("target/generated-test-sources/test"), new Shape(2730, 4096)));

        set("src/test/assets/2730x4096/origin.bmp",
                "target/generated-test-sources/test",
                new Shape(2730, 4096),
                new Rectangle(250, 300, 2730, 4096));
        assert (compareImagesArray(new FileInputStream("src/test/assets/2730x4096/set/1234x3456,250x-300.bmp"),
                new FileInputStream("target/generated-test-sources/test"), new Shape(2730, 4096)));
        file = new File("target/generated-test-sources/test");

        set("src/test/assets/2730x4096/origin.bmp",
                "target/generated-test-sources/test",
                new Shape(2730, 4096),
                new Rectangle(-20, -1500, 2730, 4096));
        assert (compareImagesArray(new FileInputStream("src/test/assets/2730x4096/set/1234x3456,250x-300,-20x1500.bmp"),
                new FileInputStream("target/generated-test-sources/test"), new Shape(2730, 4096)));
        file.delete();
    }

    void set(String src, String dest, Shape imageShape, Rectangle fragment) throws IOException {
        File file = new File(dest);
        if (file.createNewFile()) {
            BMP.createBMP(dest, imageShape);
        }
        BmpOutput output = new BmpOutput(new RandomAccessFile(file, "rw"), imageShape, fragment);
        output.write(new FileInputStream(src));
    }
}
