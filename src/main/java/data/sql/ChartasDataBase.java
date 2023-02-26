package data.sql;

import data.Shape;
import exceptions.DBException;
import exceptions.RowMissingException;
import exceptions.ShapeSizeException;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ChartasDataBase {
    private final String name;
    private final String url;

    public ChartasDataBase(String path, String name) throws DBException, IOException {
        this.name = name;
        path = path + "/" + name + ".db";
        File file = new File(path);
        if (file.createNewFile()) {
            System.out.println("DataBase file created");
        } else {
            System.out.println("DataBase file already exists");
        }
        this.url = "jdbc:sqlite:" + path;
        String sql = "CREATE TABLE IF NOT EXISTS " + name + " (Width int, Height int);";
        try (Connection con = DriverManager.getConnection(url);
             Statement statement = con.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new DBException("Error while initializing database: " + e.getMessage());
        }
    }

    public void delete() throws DBException {
        String sql = "DROP TABLE " + name;
        try (Connection con = DriverManager.getConnection(url);
             Statement statement = con.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new DBException("Error while deleting database: " + e.getMessage());
        }
    }

    public Shape getShape(int id) throws DBException, RowMissingException {
        String query = "SELECT Width, Height FROM " + name + " WHERE ROWID = " + id;
        try (Connection con = DriverManager.getConnection(url);
             Statement statement = con.createStatement()) {
            ResultSet res = statement.executeQuery(query);
            if (!res.next()) {
                throw new RowMissingException("Row for id = " + id + " doesn't exist");
            }
            int width = res.getInt(1);
            int height = res.getInt(2);
            return new Shape(width, height);
        } catch (SQLException e) {
            throw new DBException("Error while getting row from database: " + e.getMessage());
        } catch (ShapeSizeException e) {
            throw new DBException("Incorrect data in database: " + e.getMessage());
        }
    }

    public int insertRow(Shape shape) throws DBException {
        String update = "INSERT INTO " + name + " " + "(Width, Height) " + "VALUES ("
                + shape.width + ", " + shape.height + ");";
        try (Connection con = DriverManager.getConnection(url); Statement statement = con.createStatement()) {
            statement.executeUpdate(update);
            return statement.getGeneratedKeys().getInt(1);
        } catch (SQLException e) {
            throw new DBException("Error while inserting row to database: " + e.getMessage());
        }
    }

    public Set<String> clear() throws DBException {
        String query = "SELECT ROWID FROM " + name;
        ResultSet resultSet;
        try (Statement statement = DriverManager.getConnection(url).createStatement()) {
            resultSet = statement.executeQuery(query);
            Set<String> ids = new HashSet<>();
            while (resultSet.next()) {
                ids.add(resultSet.getString(1));
            }
            statement.close();
            for (String id : ids) {
                deleteRow(Integer.parseInt(id));
            }
            return ids;
        } catch (SQLException | RowMissingException e) {
            throw new DBException("Error while clearing database: " + e.getMessage());
        }
    }

    public void deleteRow(int id) throws DBException, RowMissingException {
        if (!exist(id)) {
            throw new RowMissingException("No row stored for id = " + id);
        }
        String update = "DELETE FROM " + name + " WHERE ROWID = " + id;
        try (Connection con = DriverManager.getConnection(url); Statement statement = con.createStatement()) {
            statement.executeUpdate(update);
        } catch (SQLException e) {
            throw new DBException("Error while deleting image from database: " + e.getMessage());
        }
    }

    public boolean exist(int id) throws DBException {
        String query = "SELECT ROWID FROM " + name + " WHERE ROWID = " + id;
        try (Connection con = DriverManager.getConnection(url); Statement statement = con.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            return resultSet.next();
        } catch (SQLException e) {
            throw new DBException("Error while checking if the file exists: " + e.getMessage());
        }
    }
}
