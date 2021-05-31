package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        String SQL = "CREATE TABLE IF NOT EXISTS pre_project.Users (id BIGINT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(20), lastName VARCHAR(20), age TINYINT)";
        executeStatement(statement -> {
            statement.executeUpdate(SQL);
            return null;
        });
    }

    public void dropUsersTable() {
        String SQL = "DROP TABLE IF EXISTS pre_project.Users";
        executeStatement(statement -> {
            statement.executeUpdate(SQL);
            return null;
        });
    }

    public void saveUser(String name, String lastName, byte age) {

      String SQL = "INSERT pre_project.Users (name, lastName, age) VALUES (?, ?, ?)";
        executeStatement(statement -> {
            PreparedStatement preparedStatement = statement.getConnection().prepareStatement(SQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("User с именем %s добавлен в базу данных\n", name);
            return null;
        });
    }

    public void removeUserById(long id) {
        String SQL = "DELETE FROM pre_project.Users WHERE id = " + id;
        executeStatement(statement -> {
            statement.executeUpdate(SQL);
            return null;
        });
    }

    public List<User> getAllUsers() {
        String SQL = "SELECT * FROM pre_project.Users";
        return executeStatement(statement -> {
            ResultSet r = statement.executeQuery(SQL);
            List<User> userList = new ArrayList<>();
            while (r.next()) {
                User newUser = new User(r.getString(2), r.getString(3), r.getByte(4));
                newUser.setId(r.getLong(1));
                userList.add(newUser);
            }
            return userList;
        });
    }

    public void cleanUsersTable() {
        String SQL = "TRUNCATE TABLE pre_project.Users";
        executeStatement(statement -> {
            statement.execute(SQL);
            return null;
        });
    }

    private List<User> executeStatement (Function <Statement, List<User>> executor) {
        List<User> userList = null;
        try (Connection connection = Util.getConnection(); Statement statement = connection.createStatement()) {
            userList = executor.apply(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    @FunctionalInterface
    public interface Function <Statement, List> {
        List apply (Statement statement) throws SQLException;
    }
}
