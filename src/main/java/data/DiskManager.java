package data;

import data.sql.ChartasDataBase;
import exceptions.*;
import httpserver.ResponseStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class DiskManager {
    private final ChartasDataBase dataBase;
    private final ImageHandler imageHandler;

    public DiskManager(String path, String name) throws DBException, IOException {
        File file = new File(path);
        file.mkdirs();
        dataBase = new ChartasDataBase(path, name);
        imageHandler = new ImageHandler(path);
    }

    public int createImage(Shape imageShape)
            throws DBException, FileNotCreatedException, RectangleException, ShapeSizeException {
        int id = dataBase.insertRow(imageShape);
        try {
            imageHandler.createImage(id, imageShape);
        } catch (Exception e) {
            try {
                dataBase.deleteRow(id);
            } finally {
                throw e;
            }
        }
        return id;
    }

    public ResponseStream getImageFragment(int id, Rectangle fragment) throws DBException, DataStreamException,
            IllegalFragmentException, FileNotFoundException, RowMissingException, ShapeSizeException {
        Shape imageShape = dataBase.getShape(id);
        return imageHandler.getImageFragment(id, imageShape, fragment);
    }

    public void setImageFragment(int id, Rectangle fragment, InputStream is) throws DBException, DataStreamException,
            FileNotFoundException, IllegalFragmentException, RowMissingException, ShapeSizeException {
        Shape imageShape = dataBase.getShape(id);
        imageHandler.setImageFragment(id, imageShape, fragment, is);
    }

    public void deleteImage(int id) throws DBException, RowMissingException, FileNotFoundException {
        if (!exist(id)) {
            throw new RowMissingException("Row for id = " + id + " doesn't exist");
        }
        dataBase.deleteRow(id);
        imageHandler.deleteImage(id);
    }

    public void clear() throws DBException, FileNotFoundException {
        Set<String> ids = dataBase.clear();
        for (String id : ids) {
            imageHandler.deleteImage(Integer.parseInt(id));
        }
    }

    private boolean exist(int id) throws DBException {
        return dataBase.exist(id);
    }
}
