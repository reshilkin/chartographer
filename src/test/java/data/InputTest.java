package data;

import exceptions.ShapeSizeException;
import org.junit.jupiter.api.Test;

import java.io.*;

class InputTest extends IOTest {

    @Test
    void testImage() throws IOException, ShapeSizeException {

        // (Image stored bottom-up)
        // y' = imageHeight - y - fragmentHeight

        //full image
        assert (testCorrect("src/test/assets/3x3/origin.bmp",
                "src/test/assets/3x3/origin.bmp",
                new Shape(3, 3),
                new Rectangle(0, 0, 3, 3)));
        assert (testCorrect("src/test/assets/3x3/origin.bmp",
                "src/test/assets/3x3/get/-1x-1x4x4.bmp",
                new Shape(3, 3),
                new Rectangle(-1, 3 - 4 + 1, 4, 4)));
        //large borders
        assert (testCorrect("src/test/assets/3x3/origin.bmp",
                "src/test/assets/3x3/get/-20x-20x50x50.bmp",
                new Shape(3, 3),
                new Rectangle(-20, 3 - 50 + 20, 50, 50)));
        //right-bottom corner
        assert (testCorrect("src/test/assets/3x3/origin.bmp",
                "src/test/assets/3x3/get/2x2x10x40.bmp",
                new Shape(3, 3),
                new Rectangle(2, 3 - 40 - 2, 10, 40)));
        //left-ceiling corner
        assert (testCorrect("src/test/assets/3x3/origin.bmp",
                "src/test/assets/3x3/get/-10x-20x11x21.bmp",
                new Shape(3, 3),
                new Rectangle(-10, 3 - 21 + 20, 11, 21)));
        //left-bottom corner
        assert (testCorrect("src/test/assets/3x3/origin.bmp",
                "src/test/assets/3x3/get/-23x2x25x2.bmp",
                new Shape(3, 3),
                new Rectangle(-23, 3 - 2 - 2, 25, 2)));
        //one pixel
        assert (testCorrect("src/test/assets/3x3/origin.bmp",
                "src/test/assets/3x3/get/1x1x1x1.bmp",
                new Shape(3, 3),
                new Rectangle(1, 3 - 1 - 1, 1, 1)));
        assert (testArray("src/test/assets/2730x4096/origin.bmp",
                "src/test/assets/2730x4096/get/400x900x5000x5000.bmp",
                new Shape(2730, 4096),
                new Rectangle(400, 4096 - 5000 - 900, 5000, 5000)));
        assert (testArray("src/test/assets/2730x4096/origin.bmp",
                "src/test/assets/2730x4096/get/-1000x-500x5000x5000.bmp",
                new Shape(2730, 4096),
                new Rectangle(-1000, 4096 - 5000 - (-500), 5000, 5000)));
    }

    boolean testCorrect(String source, String res, Shape imageShape, Rectangle fragment) throws IOException,
            ShapeSizeException {
        return testBytes(source, res, imageShape, fragment) && testArray(source, res, imageShape, fragment);
    }

    boolean testBytes(String source, String res, Shape imageShape, Rectangle fragment) throws IOException,
            ShapeSizeException {
        try (InputStream first = new FileInputStream(res); InputStream second = new BMPResponseStream(new File(source),
                imageShape,
                fragment)) {
            return compareImagesBytes(first, second, new Shape(fragment.width, fragment.height));
        }
    }

    boolean testArray(String source, String res, Shape imageShape, Rectangle fragment) throws IOException,
            ShapeSizeException {
        try (InputStream first = new FileInputStream(res); InputStream second = new BMPResponseStream(new File(source),
                imageShape,
                fragment)) {
            return compareImagesArray(first, second, new Shape(fragment.width, fragment.height));
        }
    }
}