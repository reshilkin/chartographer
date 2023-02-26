package data;

import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ImageHandlerTest extends IOTest {

    private final static String path = "target/generated-test-sources";

    private ImageHandler imageHandler;

    @BeforeEach
    void init() {
        imageHandler = new ImageHandler(path);
    }

    @Test
    void test() throws ShapeSizeException, FileNotCreatedException, IOException, DataStreamException,
            IllegalFragmentException {
        imageHandler.createImage(1, new Shape(3, 3));
        imageHandler.createImage(2, new Shape(350, 234));
        imageHandler.setImageFragment(1,
                new Shape(3, 3),
                new Rectangle(0, 0, 3, 3),
                new FileInputStream("src/test/assets/3x3/origin.bmp"));
        InputStream res = imageHandler.getImageFragment(1,
                new Shape(3, 3),
                new Rectangle(0, 0, 3, 3));
        assert (compareImagesArray(new FileInputStream("src/test/assets/3x3/origin.bmp"),
                res,
                new Shape(3, 3)));
        res = imageHandler.getImageFragment(1,
                new Shape(3, 3),
                new Rectangle(1, 1, 1, 1));
        assert (compareImagesArray(new FileInputStream("src/test/assets/3x3/get/1x1x1x1.bmp"),
                res,
                new Shape(1, 1)));
        res = imageHandler.getImageFragment(1,
                new Shape(3, 3),
                new Rectangle(2, 2, 10, 40));
        assert (compareImagesArray(new FileInputStream("src/test/assets/3x3/get/2x2x10x40.bmp"),
                res,
                new Shape(10, 40)));
        res = imageHandler.getImageFragment(1,
                new Shape(3, 3),
                new Rectangle(-1, -1, 4, 4));
        assert (compareImagesArray(new FileInputStream("src/test/assets/3x3/get/-1x-1x4x4.bmp"),
                res,
                new Shape(4, 4)));
        res = imageHandler.getImageFragment(1,
                new Shape(3, 3),
                new Rectangle(-10, -20, 11, 21));
        assert (compareImagesArray(new FileInputStream("src/test/assets/3x3/get/-10x-20x11x21.bmp"),
                res,
                new Shape(11, 21)));
        res = imageHandler.getImageFragment(1,
                new Shape(3, 3),
                new Rectangle(-20, -20, 50, 50));
        assert (compareImagesArray(new FileInputStream("src/test/assets/3x3/get/-20x-20x50x50.bmp"),
                res,
                new Shape(50, 50)));
        res = imageHandler.getImageFragment(1,
                new Shape(3, 3),
                new Rectangle(-23, 2, 25, 2));
        assert (compareImagesArray(new FileInputStream("src/test/assets/3x3/get/-23x2x25x2.bmp"),
                res,
                new Shape(25, 2)));

        imageHandler.setImageFragment(2,
                new Shape(350, 234),
                new Rectangle(50, 100, 350, 234),
                new FileInputStream("src/test/assets/350x234/origin.bmp"));
        res = imageHandler.getImageFragment(2,
                new Shape(350, 234),
                new Rectangle(0, 0, 350, 234));
        assert (compareImagesArray(new FileInputStream("src/test/assets/350x234/set/50x100.bmp"),
                res,
                new Shape(350, 234)));
        imageHandler.setImageFragment(2,
                new Shape(350, 234),
                new Rectangle(100, 50, 350, 234),
                new FileInputStream("src/test/assets/350x234/origin.bmp"));
        res = imageHandler.getImageFragment(2,
                new Shape(350, 234),
                new Rectangle(0, 0, 350, 234));
        assert (compareImagesArray(new FileInputStream("src/test/assets/350x234/set/50x100,100x50.bmp"),
                res,
                new Shape(350, 234)));
        imageHandler.setImageFragment(2,
                new Shape(350, 234),
                new Rectangle(-200, -50, 350, 234),
                new FileInputStream("src/test/assets/350x234/origin.bmp"));
        res = imageHandler.getImageFragment(2,
                new Shape(350, 234),
                new Rectangle(0, 0, 350, 234));
        assert (compareImagesArray(new FileInputStream("src/test/assets/350x234/set/50x100,100x50,-200x-50.bmp"),
                res,
                new Shape(350, 234)));
        Throwable thrown = assertThrows(
                IllegalFragmentException.class,
                () -> imageHandler.getImageFragment(1,
                        new Shape(1, 1),
                        new Rectangle(-1, 0, 1, 1))
        );
        assertNotNull(thrown.getMessage());
        thrown = assertThrows(
                IllegalFragmentException.class,
                () -> imageHandler.getImageFragment(1,
                        new Shape(1, 1),
                        new Rectangle(0, -1, 1, 1))
        );
        assertNotNull(thrown.getMessage());
        thrown = assertThrows(
                IllegalFragmentException.class,
                () -> imageHandler.getImageFragment(1,
                        new Shape(1, 1),
                        new Rectangle(2, 3, 1, 1))
        );
        assertNotNull(thrown.getMessage());
        thrown = assertThrows(
                IllegalFragmentException.class,
                () -> imageHandler.getImageFragment(1,
                        new Shape(1, 1),
                        new Rectangle(3, 2, 1, 1))
        );
        assertNotNull(thrown.getMessage());
        imageHandler.deleteImage(1);
        imageHandler.deleteImage(2);
        thrown = assertThrows(
                FileNotFoundException.class,
                () -> imageHandler.deleteImage(1)
        );
        assertNotNull(thrown.getMessage());
        thrown = assertThrows(
                FileNotFoundException.class,
                () -> imageHandler.deleteImage(2)
        );
        assertNotNull(thrown.getMessage());
    }
}
