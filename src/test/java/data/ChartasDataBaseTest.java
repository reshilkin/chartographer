package data;

import data.sql.ChartasDataBase;
import exceptions.DBException;
import exceptions.RowMissingException;
import exceptions.ShapeSizeException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChartasDataBaseTest {
    private final static String PATH = "target/generated-test-sources";
    private final static String NAME = "images";
    private ChartasDataBase dataBase;

    @BeforeEach
    void setUp() throws DBException, IOException {
        dataBase = new ChartasDataBase(PATH, NAME);
    }

    @AfterEach
    void tearDown() throws DBException {
        dataBase.delete();
    }

    @AfterAll
    static void deleteFile() {
        File file = new File(PATH + "/" + NAME + ".db");
        file.delete();
    }

    @Test
    void insertGetDelete() throws ShapeSizeException, DBException, RowMissingException {
        Shape test = new Shape(1, 2);
        int id = dataBase.insertRow(test);
        assert (dataBase.exist(id));
        Shape shape = dataBase.getShape(id);
        assert (test.equals(shape));
        dataBase.deleteRow(id);
        assert (!dataBase.exist(id));
        Throwable thrown = assertThrows(RowMissingException.class, () -> dataBase.deleteRow(id));
        assertNotNull(thrown.getMessage());
    }

    @Test
    void clear() throws ShapeSizeException, DBException {
        Shape test1 = new Shape(1, 2);
        Shape test2 = new Shape(10, 4);
        int id1 = dataBase.insertRow(test1);
        int id2 = dataBase.insertRow(test2);
        assert (dataBase.exist(id1));
        assert (dataBase.exist(id2));
        Set<String> res = dataBase.clear();
        assert (!dataBase.exist(id1));
        assert (!dataBase.exist(id2));
        assert (res.contains(Integer.toString(id1)));
        assert (res.contains(Integer.toString(id2)));
        res.remove(Integer.toString(id1));
        res.remove(Integer.toString(id2));
        assert (res.isEmpty());
    }
}