package data;

import exceptions.RectangleException;
import exceptions.ShapeSizeException;

import java.util.Map;

public class Rectangle extends Shape {
    public final int x;
    public final int y;

    public Rectangle(int x, int y, int width, int height) throws ShapeSizeException {
        super(width, height);
        this.x = x;
        this.y = y;
    }

    public Rectangle(Map<String, String> map) throws RectangleException, ShapeSizeException {
        super(map);
        try {
            this.x = Integer.parseInt(map.get("x"));
            this.y = Integer.parseInt(map.get("y"));
        } catch (RuntimeException e) {
            throw new RectangleException("Invalid x and y parameters for shape: x = "
                    + map.get("x") + ", y = " + map.get("y"));
        }
    }
}
