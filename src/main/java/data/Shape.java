package data;

import exceptions.ShapeSizeException;

import java.util.Map;

public class Shape {
    public final int width;
    public final int height;

    public Shape(int width, int height) throws ShapeSizeException {
        if (width <= 0 || height <= 0) {
            throw new ShapeSizeException("Invalid width and height parameters for shape: width = "
                    + width + ", height = " + height);
        }
        this.width = width;
        this.height = height;
    }

    public Shape(Map<String, String> map) throws ShapeSizeException {
        try {
            this.width = Integer.parseInt(map.get("width"));
            this.height = Integer.parseInt(map.get("height"));
            if (width <= 0 || height <= 0) {
                throw new ShapeSizeException("Invalid width and height parameters for shape: width = "
                        + width + ", height = " + height);
            }
        } catch (RuntimeException e) {
            throw new ShapeSizeException("Invalid width and height parameters for shape: width = "
                    + map.get("width") + ", height = " + map.get("height"));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Shape)) {
            return false;
        }
        return ((Shape) obj).height == height && ((Shape) obj).width == width;
    }
}
