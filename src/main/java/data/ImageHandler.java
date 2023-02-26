package data;

import exceptions.*;
import httpserver.ResponseStream;

import java.io.*;

public class ImageHandler {
    private final String path;

    public ImageHandler(String path) {
        File file = new File(path);
        file.mkdirs();
        this.path = path;
    }

    private String fileName(int id) {
        return path + "/" + id + ".bmp";
    }

    public void createImage(int id, Shape s) throws FileNotCreatedException, ShapeSizeException {
        try {
            if (s.width > 20000 || s.height > 50000) {
                throw new ShapeSizeException("Invalid arguments for image shape: width = "
                        + s.width + ", height = " + s.height);
            }
            BMP.createBMP(fileName(id), s);
        } catch (IOException e) {
            throw new FileNotCreatedException("Failed to create file for id = " + id + ": " + e.getMessage());
        }
    }

    public File getImage(int id) throws FileNotFoundException {
        File file = new File(fileName(id));
        if (!file.exists()) {
            throw new FileNotFoundException("No image for id = " + id);
        }
        return file;
    }

    public ResponseStream getImageFragment(int id, Shape imageShape, Rectangle fragment)
            throws DataStreamException, IllegalFragmentException, FileNotFoundException, ShapeSizeException {
        // Created image stores pixels bottom-up, so we need to take fragment from the other side
        fragment = turnOver(fragment, imageShape);
        if (fragment.width > 5000 || fragment.height > 5000
                || fragment.x + fragment.width <= 0 || fragment.x >= imageShape.width
                || fragment.y + fragment.height <= 0 || fragment.y >= imageShape.height) {
            throw new IllegalFragmentException("Invalid rectangle for get request (id = " + id + ")");
        }
        File file = getImage(id);
        try {
            return new BMPResponseStream(file, imageShape, fragment);
        } catch (IOException e) {
            throw new DataStreamException("Error while creating image input stream: " + e.getMessage());
        }
    }

    public void setImageFragment(int id, Shape imageShape, Rectangle fragment, InputStream is)
            throws DataStreamException, IllegalFragmentException, FileNotFoundException, ShapeSizeException {
        // Created image stores pixels bottom-up, so we need to fill fragment from the other side
        fragment = turnOver(fragment, imageShape);
        if (fragment.width > 20000 || fragment.height > 50000
                || fragment.x + fragment.width <= 0 || fragment.x >= imageShape.width
                || fragment.y + fragment.height <= 0 || fragment.y >= imageShape.height) {
            throw new IllegalFragmentException("Invalid rectangle for set request (id = " + id + ")");
        }
        File file = getImage(id);
        try {
            BmpOutput out = new BmpOutput(new RandomAccessFile(file, "rw"), imageShape, fragment);
            out.write(is);
        } catch (IOException e) {
            throw new DataStreamException("Error while changing image fragment: " + e.getMessage());
        }
    }

    public void deleteImage(int id) throws FileNotFoundException {
        File file = new File(fileName(id));
        if (!file.exists()) {
            throw new FileNotFoundException("No file for id = " + id);
        }
        file.delete();
    }

    private Rectangle turnOver(Rectangle fragment, Shape imageShape) throws ShapeSizeException {
        return new Rectangle(fragment.x, -fragment.y - fragment.height + imageShape.height,
                fragment.width, fragment.height);
    }
}
